package com.exercise.service;

import com.exercise.dto.WeatherResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class WeatherService {

    @Value("${openapi.weather.key}")
    private String apiKey;

    private static class GridPoint { int nx; int ny; GridPoint(int nx, int ny) { this.nx = nx; this.ny = ny; } }

    public WeatherResponse getWeatherData(double lat, double lon) {
        // 위도, 경도를 기상청 격자 x, y로 변환
        GridPoint point = convertGrid(lat, lon);

        // 오늘 날짜 및 기준 시간 세팅
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String tomorrow = LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String dayAfter = LocalDate.now().plusDays(2).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String baseTime = "0500";

        // 기상청 날씨 API 호출
        String url = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst";
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(url);
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);
        RestClient weatherClient = RestClient.builder().uriBuilderFactory(factory).build();

        // 미세먼지 API 호출 (에어코리아)
        String dustUrl = "http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getCtprvnRltmMesureDnsty";
        DefaultUriBuilderFactory dustFactory = new DefaultUriBuilderFactory(dustUrl);
        dustFactory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);
        RestClient dustClient = RestClient.builder()
                .uriBuilderFactory(dustFactory)
                .defaultHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36") // ✨ 이 줄이 추가됩니다!
                .build();

        try {
            // 날씨 호출
            String jsonResponse = weatherClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("serviceKey", apiKey)
                            .queryParam("pageNo", "1")
                            .queryParam("numOfRows", "1000")
                            .queryParam("dataType", "JSON")
                            .queryParam("base_date", today)
                            .queryParam("base_time", baseTime)
                            .queryParam("nx", point.nx)
                            .queryParam("ny", point.ny)
                            .build())
                    .retrieve()
                    .body(String.class);

            // 미세먼지 호출
            String sidoName = convertLatToSido(lat);
            String dustJson = dustClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("serviceKey", apiKey)
                            .queryParam("returnType", "json")
                            .queryParam("numOfRows", "1") // 해당 시도의 최상단 측정소 한 곳만 가볍게 긁어옴
                            .queryParam("pageNo", "1")
                            .queryParam("sidoName", sidoName)
                            .queryParam("ver", "1.0")
                            .build())
                    .retrieve()
                    .body(String.class);

            ObjectMapper objectMapper = new ObjectMapper();

            // 날씨 JSON 파싱
            JsonNode root = objectMapper.readTree(jsonResponse);
            JsonNode itemArray = root.path("response").path("body").path("items").path("item");

            // 추출할 변수 초기화
            String todaySky = "☀️ 맑음"; String todayREH = "-";
            String tomorrowSky = "☀️ 맑음"; String tomorrowREH = "-";
            String dayAfterSky = "☀️ 맑음"; String dayAfterREH = "-";

            // 최저/최고 기온 변수 초기화
            String todayTMN = "-"; String todayTMX = "-";
            String tomorrowTMN = "-"; String tomorrowTMX = "-";
            String dayAfterTMN = "-"; String dayAfterTMX = "-";

            for (JsonNode item : itemArray) {
                String fcstDate = item.path("fcstDate").asText();
                String category = item.path("category").asText();
                String fcstValue = item.path("fcstValue").asText();

                // 하늘 상태(SKY) 또는 강수 형태(PTY) 파싱
                if (category.equals("SKY") || category.equals("PTY")) {
                    String weatherText = parseWeather(itemArray, fcstDate);
                    if (fcstDate.equals(today)) todaySky = weatherText;
                    else if (fcstDate.equals(tomorrow)) tomorrowSky = weatherText;
                    else if (fcstDate.equals(dayAfter)) dayAfterSky = weatherText;
                }

                // 습도(REH) 파싱
                if (category.equals("REH")) {
                    if (fcstDate.equals(today)) todayREH = fcstValue;
                    else if (fcstDate.equals(tomorrow)) tomorrowREH = fcstValue;
                    else if (fcstDate.equals(dayAfter)) dayAfterREH = fcstValue;
                }

                // 최저 기온(TMN) 날짜별 파싱
                if (category.equals("TMN")) {
                    String tmnVal = Math.round(Double.parseDouble(fcstValue)) + "°C";
                    if (fcstDate.equals(today)) todayTMN = tmnVal;
                    else if (fcstDate.equals(tomorrow)) tomorrowTMN = tmnVal;
                    else if (fcstDate.equals(dayAfter)) dayAfterTMN = tmnVal;
                }

                // 최고 기온(TMX) 날짜별 파싱
                if (category.equals("TMX")) {
                    String tmxVal = Math.round(Double.parseDouble(fcstValue)) + "°C";
                    if (fcstDate.equals(today)) todayTMX = tmxVal;
                    else if (fcstDate.equals(tomorrow)) tomorrowTMX = tmxVal;
                    else if (fcstDate.equals(dayAfter)) dayAfterTMX = tmxVal;
                }

            }

            // 미세먼지 JSON 파싱
            JsonNode dustRoot = objectMapper.readTree(dustJson);
            JsonNode itemsNode = dustRoot.path("response").path("body").path("items");

            String pm10Grade = "정보없음"; String pm25Grade = "정보없음";
            String pm10Value = "-"; String pm25Value = "-";

            if (itemsNode.isArray() && !itemsNode.isEmpty()) {
                JsonNode dustItem = itemsNode.get(0);
                // 미세먼지 등급별 좋음(1), 보통(2), 나쁨(3), 매우나쁨(4)으로 표시 + 수치 표시
                pm10Grade = parseDustGrade(dustItem.path("pm10Grade").asText());
                pm25Grade = parseDustGrade(dustItem.path("pm25Grade").asText());
                pm10Value = dustItem.path("pm10Value").asText("-");
                pm25Value = dustItem.path("pm25Value").asText("-");
            }


            // 결과 조립하여 최종 수정된 record DTO 리턴
            return new WeatherResponse(
                    todaySky, todayREH, todayTMN, todayTMX,
                    tomorrowSky, tomorrowREH, tomorrowTMN, tomorrowTMX,
                    dayAfterSky, dayAfterREH, dayAfterTMN, dayAfterTMX,
                    pm10Grade, pm10Value,
                    pm25Grade, pm25Value
            );

        }catch(Exception e) {

            e.printStackTrace();

            return new WeatherResponse(
                    "⚠️ 오류", "-", "-", "-",
                    "⚠️ 오류", "-", "-", "-",
                    "⚠️ 오류", "-", "-", "-",
                    "-", "-", "-", "-"
            );

        }

    }

    // 날씨 코드(하늘상태+강수형태)를 직관적인 이모지와 텍스트로 변환하는 유틸 메서드
    private String parseWeather(JsonNode itemArray, String targetDate) {
        int sky = 1; int pty = 0;
        for (JsonNode item : itemArray) {
            if (item.path("fcstDate").asText().equals(targetDate)) {
                if (item.path("category").asText().equals("SKY")) sky = item.path("fcstValue").asInt();
                if (item.path("category").asText().equals("PTY")) pty = item.path("fcstValue").asInt();
            }
        }
        if (pty > 0) {
            if (pty == 1 || pty == 4) return "🌧️ 비";
            if (pty == 2) return "🌨️ 진눈깨비";
            if (pty == 3) return "❄️ 눈";
        }
        if (sky == 3) return "☁️ 구름많음";
        if (sky == 4) return "☁️ 흐림";
        return "☀️ 맑음";
    }

    // 미세먼지 등급 코드를 한글 텍스트로 치환하는 메서드
    private String parseDustGrade(String grade) {
        return switch (grade) {
            case "1" -> "좋음";
            case "2" -> "보통";
            case "3" -> "나쁨";
            case "4" -> "매우나쁨";
            default -> "정보없음";
        };
    }

    // 대략적인 위도 기준 미세먼지 조회용 시도명 대입 유틸 메서드
    private String convertLatToSido(double lat) {
        if (lat > 37.7) return "경기";
        if (lat > 37.4) return "서울";
        if (lat > 36.5) return "충남";
        if (lat > 35.8) return "경북";
        if (lat > 35.0) return "전남";
        return "부산";
    }

    // 위경도 -> 기상청 XY 격자 변환 공식 코드
    private GridPoint convertGrid(double lat, double lon) {
        double RE = 6371.00877; double GRID = 5.0; double SLAT1 = 30.0; double SLAT2 = 60.0;
        double OLON = 126.0; double OLAT = 38.0; double XO = 43; double YO = 136;
        double DEGRAD = Math.PI / 180.0; double re = RE / GRID;
        double sn = Math.tan(Math.PI * 0.25 + SLAT2 * DEGRAD * 0.5) / Math.tan(Math.PI * 0.25 + SLAT1 * DEGRAD * 0.5);
        sn = Math.log(Math.cos(SLAT1 * DEGRAD) / Math.cos(SLAT2 * DEGRAD)) / Math.log(sn);
        double sf = Math.tan(Math.PI * 0.25 + SLAT1 * DEGRAD * 0.5); sf = Math.pow(sf, sn) * Math.cos(SLAT1 * DEGRAD) / sn;
        double ro = Math.tan(Math.PI * 0.25 + OLAT * DEGRAD * 0.5); ro = re * sf / Math.pow(ro, sn);
        double ra = Math.tan(Math.PI * 0.25 + lat * DEGRAD * 0.5); ra = re * sf / Math.pow(ra, sn);
        double theta = lon * DEGRAD - OLON * DEGRAD;
        if (theta > Math.PI) theta -= 2.0 * Math.PI; if (theta < -Math.PI) theta += 2.0 * Math.PI; theta *= sn;
        int nx = (int) Math.floor(ra * Math.sin(theta) + XO + 0.5);
        int ny = (int) Math.floor(ro - ra * Math.cos(theta) + YO + 0.5);
        return new GridPoint(nx, ny);
    }

}
