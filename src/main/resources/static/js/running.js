$(document).ready(function() {

    // 선택자 수정
    const fileInput = $('#csvFileInput');
    const uploadBtn = $('#uploadCsvBtn');
    const tableBody = $('#runningRecords');
    const noDataMsg = $('#noDataMessage');

    const totalDist = $('#totalDistance');
    const totalTime = $('#totalTime');
    const avgPace = $('#averagePace');
    const totalCal = $('#totalCalories');

    // 테이블에 데이터 렌더링
    function renderTable(data) {
        let rows = '';
        $.each(data, function(index, record) {
            // [수정] 날짜 가공: Spring의 LocalDateTime은 '2025-05-27T15:19:35' 형식으로 오므로 'T'로 자릅니다.
            const dateOnly = record.startTime ? record.startTime.split('T')[0] : '-';

            // [수정] 거리 가공: m -> km
            const km = record.distance ? (record.distance / 1000).toFixed(2) : '0.00';

            // [추가] 시간 가공: 엔티티에 durationText가 없으므로 durationSeconds로 직접 계산합니다.
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
        tableBody.find('tr:not(#noDataMessage)').remove();
        tableBody.append(rows);
    }

    // 요약 카드 업데이트
    function updateSummaryCards(records) {
        let totalDistVal = 0;
        let totalSecVal = 0;
        let totalCalVal = 0;

        $.each(records, function(_, record) {
            totalDistVal += (record.distance / 1000); // m -> km
            totalSecVal += record.durationSeconds;
            totalCalVal += record.calorie;
        });

        totalDist.text(totalDistVal.toFixed(2) + ' km');
        totalCal.text(Math.round(totalCalVal).toLocaleString() + ' kcal');

        const hours = Math.floor(totalSecVal / 3600);
        const minutes = Math.floor((totalSecVal % 3600) / 60);
        totalTime.text(`${hours}시간 ${minutes}분`);

        // 평균 페이스 계산
        if (totalDistVal > 0) {
            const avgSecPerKm = totalSecVal / totalDistVal;
            const avgPaceMin = Math.floor(avgSecPerKm / 60);
            const avgPaceSec = Math.round(avgSecPerKm % 60);
            avgPace.text(`${avgPaceMin}'${avgPaceSec.toString().padStart(2, '0')}" /km`);
        }
    }

    function resetSummary() {
        totalDist.text('0.00 km');
        totalTime.text('0시간 0분');
        avgPace.text("0'00\" /km");
        totalCal.text('0 kcal');
    }

    // CSV 파일 업로드
    uploadBtn.on('click', function() {
        const file = fileInput[0].files[0];
        if (!file) {
            alert("CSV 파일을 선택해주세요.");
            return;
        }

        const formData = new FormData();
        formData.append("file", file);

        uploadBtn.prop('disabled', true).html('<i class="fas fa-spinner fa-spin me-2"></i> 업로드 중...');

        $.ajax({
            url: '/api/running/running-records',
            method: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            success: function(response) {
                alert("업로드가 완료되었습니다!");
                if(response && response.length > 0) {
                    noDataMsg.hide();
                    renderTable(response);          // 서버에서 받은 데이터 테이블 그리기
                    updateSummaryCards(response);   // 서버에서 받은 데이터 카드 업데이트
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