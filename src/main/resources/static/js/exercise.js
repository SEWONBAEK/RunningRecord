$(document).ready(function() {

    // 페이지 로드 시 위치 기반 날씨 조회
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(successGPS, errorGPS);
    } else {
        $('#weather-loading').text("GPS를 지원하지 않는 브라우저입니다.");
    }

    function successGPS(position) {
        const lat = position.coords.latitude;
        const lon = position.coords.longitude;

        // 스프링 부트 날씨 API 호출
        $.ajax({
            url: `/api/weather?lat=${lat}&lon=${lon}`,
            method: 'GET',
            success: function(data) {
                // 로딩 메시지 숨기고 날씨 컨텐츠 표시
                $('#weather-loading').hide();
                $('#weather-info').show();

                // 오늘 날짜 데이터 매핑
                $('#today-sky').text(data.todaySky);
                $('#today-humidity').text(data.todayREH + '%');
                $('#today-temp').text(`${data.todayTMN} / ${data.todayTMX}`);

                // 내일 날짜 데이터 매핑
                $('#tomorrow-sky').text(data.tomorrowSky);
                $('#tomorrow-humidity').text(data.tomorrowREH + '%');
                $('#tomorrow-temp').text(`${data.tomorrowTMN} / ${data.tomorrowTMX}`);

                // 모레 날짜 데이터 매핑
                $('#dayafter-sky').text(data.dayAfterSky);
                $('#dayafter-humidity').text(data.dayAfterREH + '%');
                $('#dayafter-temp').text(`${data.dayAfterTMN} / ${data.dayAfterTMX}`);

                // 미세먼지 데이터 매핑
                $('#dust-pm10').text(`${data.pm10Grade} (${data.pm10Value} ㎍/㎥)`);
                $('#dust-pm25').text(`${data.pm25Grade} (${data.pm25Value} ㎍/㎥)`);
            },
            error: function() {
                $('#weather-loading').text("날씨 정보를 불러오지 못했습니다.");
            }
        });
    }

    function errorGPS() {
        $('#weather-loading').text("위치 정보 권한을 허용해주세요.");
    }

    // 공통 선택자
    const fileInput = $('#csvFileInput');
    const uploadBtn = $('#uploadCsvBtn');

    // 달리기 선택자
    const runTableBody = $('#runningRecords');
    const runNoDataMsg = $('#noDataMessage');
    const runTotalDist = $('#totalDistance');
    const runTotalTime = $('#totalTime');
    const runAvgPace = $('#averagePace');
    const runTotalCal = $('#totalCalories');

    // 걷기 선택자
    const walkTableBody = $('#walkingRecords');
    const walkNoDataMsg = $('#noDataMessageWalk');
    const walkTotalDist = $('#totalDistanceWalk');
    const walkTotalTime = $('#totalTimeWalk');
    const walkTotalSteps = $('#totalStepsWalk'); // 걷기는 페이스 대신 '걸음 수'
    const walkTotalCal  = $('#totalCaloriesWalk');

    // 달리기(RUN) 처리 함수
    function renderRunningTable(data) {
        let rows = '';
        $.each(data, function(index, record) {

            // 날짜 가공 : Spring의 LocalDateTime은 '2025-05-27T15:19:35' 형식으로 오므로 'T'로 자릅니다.
            const dateOnly = record.startTime ? record.startTime.split('T')[0] : '-';

            // 거리 가공 : m -> km
            const km = record.distance ? (record.distance / 1000).toFixed(2) : '0.00';

            // 시간 가공: 엔티티에 durationText가 없으므로 durationSeconds로 직접 계산합니다.
            let durationText = '00:00';
            if (record.durationSeconds) {
                const h = Math.floor(record.durationSeconds / 3600);
                const m = Math.floor((record.durationSeconds % 3600) / 60);
                const s = record.durationSeconds % 60;
                durationText = `${h > 0 ? h + ':' : ''}${m.toString().padStart(2, '0')}:${s.toString().padStart(2, '0')}`;
            }

            rows += `
                <tr>
                    <td class="fw-medium">${dateOnly}</td>
                    <td><span class="text-primary fw-bold">${km}</span> km</td>
                    <td>${durationText}</td>
                    <td><span class="badge bg-light text-dark border">${record.pace || "0'00\""}</span></td>
                    <td><i class="fas fa-heartbeat text-danger me-1"></i>${record.meanHeartRate || '-'}</td>
                    <td>${record.calorie ? Math.round(record.calorie).toLocaleString() : 0} kcal</td>
                </tr>
            `;
        });
        // 기존 행 삭제 후 추가 (메시지 행 제외)
        runTableBody.find('tr:not(#noDataMessage)').remove();
        runTableBody.append(rows);
    }

    // 달리기 데이터 계산
    function updateRunningSummary(records) {
        let totalDistVal = 0;
        let totalSecVal = 0;
        let totalCalVal = 0;

        $.each(records, function(_, record) {
            totalDistVal += (record.distance / 1000); // m -> km
            totalSecVal += record.durationSeconds;
            totalCalVal += record.calorie;
        });

        runTotalDist.text(totalDistVal.toFixed(2) + ' km');
        runTotalCal.text(Math.round(totalCalVal).toLocaleString() + ' kcal');

        const hours = Math.floor(totalSecVal / 3600);
        const minutes = Math.floor((totalSecVal % 3600) / 60);
        runTotalTime.text(`${hours}시간 ${minutes}분`);

        // 평균 페이스 계산
        if (totalDistVal > 0) {
            const avgSecPerKm = totalSecVal / totalDistVal;
            const avgPaceMin = Math.floor(avgSecPerKm / 60);
            const avgPaceSec = Math.round(avgSecPerKm % 60);
            runAvgPace.text(`${avgPaceMin}'${avgPaceSec.toString().padStart(2, '0')}" /km`);
        }
    }

    // 걷기(WALK) 처리 함수
    function renderWalkingTable(data) {
        let rows = '';
        $.each(data, function(index, record) {
            const dateOnly = record.startTime ? record.startTime.split('T')[0] : '-';
            const km = record.distance ? (record.distance / 1000).toFixed(2) : '0.00';

            let durationText = '00:00';
            if (record.durationSeconds) {
                const h = Math.floor(record.durationSeconds / 3600);
                const m = Math.floor((record.durationSeconds % 3600) / 60);
                const s = record.durationSeconds % 60;
                durationText = `${h > 0 ? h + ':' : ''}${m.toString().padStart(2, '0')}:${s.toString().padStart(2, '0')}`;
            }

            rows += `
                <tr>
                    <td class="fw-medium">${dateOnly}</td>
                    <td><span class="text-primary fw-bold">${km}</span> km</td>
                    <td>${durationText}</td>
                    <td><span class="badge bg-light text-dark border">${record.count.toLocaleString()} 걸음</span></td>
                    <td>${record.calorie ? Math.round(record.calorie).toLocaleString() : 0} kcal</td>
                </tr>
            `;
        });
        walkTableBody.find('tr:not(#noDataMessageWalk)').remove();
        walkTableBody.append(rows);
    }

    // 걷기 데이터 계산
    function updateWalkingSummary(records) {
        let totalDistVal = 0;
        let totalSecVal = 0;
        let totalStepsVal = 0;
        let totalCalVal = 0;

        $.each(records, function(_, record) {
            totalDistVal += (record.distance / 1000);
            totalSecVal += record.durationSeconds;
            totalStepsVal += record.count;
            totalCalVal += record.calorie;
        });

        walkTotalDist.text(totalDistVal.toFixed(2) + ' km');
        walkTotalCal.text(Math.round(totalCalVal).toLocaleString() + ' kcal');
        walkTotalSteps.text(totalStepsVal.toLocaleString() + ' 걸음');

        const hours = Math.floor(totalSecVal / 3600);
        const minutes = Math.floor((totalSecVal % 3600) / 60);
        walkTotalTime.text(`${hours}시간 ${minutes}분`);
    }

    // CSV 파일 업로드
    uploadBtn.on('click', function() {
        const file = fileInput[0].files[0];
        if (!file) {
            alert("CSV 파일을 선택해주세요.");
            return;
        }

        // 현재 어떤 화면 탭이 활성화되어 있는지 확인
        const isRunningActive = $('#tab-run').hasClass('active');

        // 활성화 상태에 따라 타겟 컨트롤러 URL 주소 분기
        const targetUrl = isRunningActive ? '/api/running/running-records' : '/api/walking/walking-records';

        const formData = new FormData();
        formData.append("file", file);

        uploadBtn.prop('disabled', true).html('<i class="fas fa-spinner fa-spin me-2"></i> 업로드 중...');
        console.log(targetUrl);
        $.ajax({
            url: targetUrl,
            method: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            success: function(response) {
                alert("업로드가 완료되었습니다!");
                if(response && response.length > 0) {
                    if(isRunningActive) {
                        // 달리기 데이터 테이블 및 카드 업데이트
                        runNoDataMsg.hide();
                        renderRunningTable(response);
                        updateRunningSummary(response);
                    } else {
                        // 걷기 데이터 테이블 및 카드 업데이트
                        walkNoDataMsg.hide();
                        renderWalkingTable(response);
                        updateWalkingSummary(response);
                    }

                }
            },
            error: function() {
                alert("파일 처리 중 오류가 발생했습니다. CSV 형식을 확인하세요.");
            },
            complete: function() {
                uploadBtn.prop('disabled', false).html('<i class="fas fa-upload me-2"></i> 기록 업로드');
                fileInput.val(''); // 파일 입력창 초기화
            }
        });
    });
});

function switchExercise(type) {
    // 1. 두 영역을 모두 숨깁니다.
    $('#content-run, #content-walk').hide();

    // 2. 탭 버튼의 active 클래스를 모두 제거합니다.
    $('#tab-run, #tab-walk').removeClass('active');

    // 3. 선택된 영역만 보여주고 버튼을 활성화합니다.
    if (type === 'run') {
        $('#content-run').show();
        $('#tab-run').addClass('active');
    } else if (type === 'walk') {
        $('#content-walk').show();
        $('#tab-walk').addClass('active');
    }
}