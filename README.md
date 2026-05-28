<div align="center">
  <img src="./images/Running_Thumbnail.png" width="80%">
  <h3>개인 러닝 기록</h3>
</div>

## ⌨️ 기간

- **2026.05.21 ~ 2026.02.28(7일)**

<a name="tableContents"></a>

<br/>

## 🔎 목차

1. <a href="#subject">🎯 주제</a>
1. <a href="#mainContents">⭐️ 주요 기능</a>
1. <a href="#systemArchitecture">⚙ 시스템 아키텍쳐</a>
1. <a href="#skills">🛠️ 기술 스택</a>
1. <a href="#erd">💾 ERD</a>
1. <a href="#contents">🖥️ 화면 소개</a>
1. <a href="#developers">👥 팀원 소개</a>

<br/>

<!------- 주제 시작 -------->

## 🎯 주제

<a name="subject"></a>

**Ajax** **REST API** 이용한 삼성 헬스 데이터를 추출하여 개인 러닝 기록<br />
<br />
삼성 헬스의 CSV 파일의 달리기 기록 데이터를 가공하여 필요한 러닝 기록만<br />
화면에 표시하여 내가 운동한 기록을 확인할 수 있는 프로젝트입니다.

<br />

**주요 기능**

- 삼성 헬스에서 받은 데이터 파일(CSV)을 가공하여 내가 여태 운동한 기록을 보여줍니다.
  
<div align="right"><a href="#tableContents">목차로 이동</a></div>

<br/>

<!------- 주요 기능 시작 -------->

## ⭐️ 주요 기능

<a name="mainContents"></a>

### 공통

- CSV 파일을 업로드하여 등록하면 업로드한 CSV 파일 데이터를 분석합니다.
- 달리기 데이터(코드 : 1002)만 추출합니다.
- 출발 시간, 거리(m), 칼로리(kcal), 심박수(bpm), 평균 속도(m/s) 데이터를 추출합니다.
- 평균 페이스를 거리와 평균 속도로 분당 페이스와 운동 시간을 계산합니다.
- 출발 시간에 있는 날짜 데이터만 추출하여 날짜를 표시합니다.

---

<div align="right"><a href="#tableContents">목차로 이동</a></div>

<br/>


<!------- 시스템 아키텍쳐 시작 -------->

## ⚙ 시스템 아키텍쳐

<a name="systemArchitecture"></a>

<img src="./images/eLMS_System Architecture.png">

- Frontend: HTML5, CSS3, JavaScript를 활용하여 깔끔하고 가독성 좋은 UI와 Ajax를 이용한 비동기 통신 환경을 구축 
- Backend & DB: Spring 프레임워크를 사용하여 서버를 구현하였으며, MySQL을 통해 교수, 학생 데이터를 관리

본 프로젝트는 Spring MVC 아키텍처를 기반으로 설계된 비대면 과제 관리 플랫폼입니다.<br />
Ajax를 활용한 비동기 통신 구조를 채택하여 화면 전환 없는 매끄러운 사용자 경험을 제공하며,<br />
관리자·교수·학생 각 사용자 역할에 최적화된 비즈니스 로직을 독립적으로 처리할 수 있도록 시스템 아키텍처를 구축하였습니다.

<div align="right"><a href="#tableContents">목차로 이동</a></div>

<br/>

<!------- 기술 스택 시작 -------->

## 🛠️ 기술 스택

<a name="skills"></a>

### 💻 FrontEnd

![HTML5](https://img.shields.io/badge/html5-%23E34F26.svg?style=for-the-badge&logo=html5&logoColor=white)
![CSS3](https://img.shields.io/badge/css3-%231572B6.svg?style=for-the-badge&logo=css3&logoColor=white)
![Ajax](https://img.shields.io/badge/Ajax-%23ffffff.svg?style=for-the-badge&logo=jquery&logoColor=black)
![Figma](https://img.shields.io/badge/figma-%23F24E1E.svg?style=for-the-badge&logo=figma&logoColor=white)
---

### ⚙️ BackEnd

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Mysql](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mariadb&logoColor=white)
---

### 🤝 Collaboration

![GitHub](https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white)
![Git](https://img.shields.io/badge/git-%23F05033.svg?style=for-the-badge&logo=git&logoColor=white)
---

<div align="right"><a href="#tableContents">목차로 이동</a></div>

<br/>

<!------- ERD 시작 -------->

## 💾 ERD
<a name="erd"></a>
<img src="./images/eLMS_ERD.png">

<div align="right"><a href="#tableContents">목차로 이동</a></div>

<br/>

<!------- 화면 소개 시작 -------->

<a name="contents"></a>

<br/>

## 🖥️ 화면 소개

### 1. 공통

<table>
    <tr>
        <td align="center" width="200">
            <h5>로그인 페이지</h5>
            <img src="./images/common_login.png" alt="로그인" width="100%" />  
        </td>
        <td align="center" width="200">
            <h5>학생 회원가입 모달</h5>
            <img src="./images/common_sign_up_student.png" alt="학생 회원가입" width="100%" />  
        </td> 
        <td align="center" width="200">
            <h5>교수 회원가입 모달</h5>
            <img src="./images/common_sign_up_teacher.png" alt="메인하단" width="100%" />
        </td>
        <td align="center" width="200">
            <h5>개인정보변경</h5>
            <img src="./images/common_info_edit.png" alt="개인정보변경" width="100%" />
        </td>
        <td align="center" width="200">
            <h5>아이디 찾기 완료</h5>
            <img src="./images/common_id_find_ok.png" alt="아이디 찾기 완료" width="100%" />
        </td>
        <td align="center" width="200">
            <h5>비밀번호 재설정</h5>
            <img src="./images/common_password_edit.png" alt="비밀번호 재설정" width="100%" />
        </td>
    </tr>
    <tr>
      <td align="center">
        <div>✔ 회원가입한 이메일과 비밀번호 입력 후 로그인</div>
        <div>✔ 프론트엔드, 백엔드 유효성 검사</div>
      </td>
      <td align="center">
        <div>✔ 학생 회원가입 모달창</div>
        <div>✔ 이메일 중복 검사 등 회원가입에 필요한 유효성 검사</div>
      </td>
      <td align="center">
        <div>✔ 교수 회원가입 모달창</div>
        <div>✔ DB 유저타입을 학생과 구분되어 가입</div>
      </td>
      <td align="center">
        <div>✔ 비밀번호 변경 및 휴대폰 변경 가능(선택사항)</div>
      </td>
      <td align="center">
        <div>✔ 아이디 찾기에 필요한 정보를 입력 후 정보에 해당되는 이메일이 있으면 표시</div>
      </td>
      <td align="center">
        <div>✔ 비밀번호 찾기에 필요한 정보 입력 후 정보에 해당되는 유저가 있으면 비밀번호 재설정 가능</div>
      </td>
    </tr>
</table>

### 2. 관리자

<table>
    <tr>
        <td align="center" width="200">
            <h5>공지사항 목록</h5>
            <img src="./images/admin_notice_list.png" alt="관리자 공지사항 목록" width="200" />  
        </td>
        <td align="center" width="200">
            <h5>공지사항 작성</h5>
            <img src="./images/admin_notice_write.png" alt="관리자 공지사항 작성" width="200" />  
        </td>
        <td align="center" width="200">
            <h5>공지사항 수정</h5>
            <img src="./images/admin_notice_edit.png" alt="관리자 공지사항 수정" width="200" />  
        </td>
        <td align="center" width="200">
            <h5>공지사항 상세</h5>
            <img src="./images/admin_notice_view.png" alt="관리자 공지사항 상세" width="200" />  
        </td>
        <td align="center" width="200">
            <h5>교수관리</h5>
            <img src="./images/admin_teacher_management.png" alt="관리자 교수관리" width="200" />  
        </td>
        <td align="center" width="200">
            <h5>학생관리</h5>
            <img src="./images/admin_student_management.png" alt="관리자 학생관리" width="200" />  
        </td>
    </tr>
    <tr>
      <td align="center">
        <div>✔ 관리자 로그인 시 초기 페이지입니다.</div>
        <div>✔ 공지사항 목록을 보여주는 페이지입니다.</div>
      </td>
      <td align="center">
        <div>✔ 공지사항을 작성하는 페이지입니다.</div>
        <div>✔ 첨부파일은 단건으로 업로드 가능합니다.</div>
      </td>
      <td align="center">
        <div>✔ 공지사항을 수정하는 페이지입니다.</div>
      </td>
      <td align="center">
        <div>✔ 공지사항 상세 페이지입니다.</div>
      </td>
      <td align="center">
        <div>✔ 관리자가 교수의 재적관리 가능한 페이지입니다.</div>
        <div>✔ 휴직 또는 퇴직인 교수는 교수 기능이 제한됩니다.</div>
      </td>
      <td align="center">
        <div>✔ 관리자가 학생의 재적관리 가능한 페이지입니다.</div>
        <div>✔ 휴학 또는 퇴학 등인 학생은 학생 기능이 제한됩니다.</div>
      </td>
    </tr>
</table>

### 3. 교수

<table>
    <tr>
        <td align="center" width="200">
            <h5>메인 페이지</h5>
            <img src="./images/teacher_subject_management.png" alt="" width="200" />  
        </td>
        <td align="center" width="200">
            <h5>과목 등록 모달</h5>
            <img src="./images/teacher_subject_add.png" alt="" width="200" />  
        </td>
        <td align="center" width="200">
            <h5>과목 수정 모달</h5>
            <img src="./images/teacher_subject_edit.png" alt="" width="200" />  
        </td>
        <td align="center" width="200">
            <h5>과제 게시판 목록</h5>
            <img src="./images/teacher_submission_list.png" alt="" width="200" />  
        </td>
        <td align="center" width="200">
            <h5>과제 등록</h5>
            <img src="./images/teacher_submission_write.png" alt="" width="200" />  
        </td>
        <td align="center" width="200">
            <h5>과제 수정</h5>
            <img src="./images/teacher_submission_edit.png" alt="" width="200" />  
        </td>
        <td align="center" width="200">
            <h5>과제 정보 조회</h5>
            <img src="./images/teacher_submission_view.png" alt="" width="200" />  
        </td>
    </tr>
    <tr>
      <td align="center">
        <div>✔ 로그인 시 초기 페이지입니다.</div>
        <div>✔ 과목 관리 목록 페이지입니다.</div>
      </td>
      <td align="center">
        <div>✔ 과목 등록에 필요한 정보를 입력하는 모달창을 띄워줍니다.</div>
      </td>
      <td align="center">
        <div>✔ 과목 수정에 필요한 정보를 수정하는 모달창을 띄워줍니다.</div>
      </td>
      <td align="center">
        <div>✔ 과제 제출 및 채점 현황을 보여주는 리스트입니다.</div>
      </td>
      <td align="center">
        <div>✔ 과제 등록에 필요한 정보를 입력하는 페이지입니다.</div>
      </td>
      <td align="center">
        <div>✔ 과제 수정에 필요한 정보를 수정하는 페이지입니다.</div>
      </td>
      <td align="center">
        <div>✔ 등록한 과제 정보를 조회하는 페이지입니다.</div>
      </td>
    </tr>
</table>
<table>
    <tr>
        <td align="center" width="200">
            <h5>공지사항 목록</h5>
            <img src="./images/teacher_notice_list.png" alt="교수 공지사항 리스트" width="200" />  
        </td>
        <td align="center" width="200">
            <h5>공지사항 상세</h5>
            <img src="./images/teacher_notice_view.png" alt="교수 공지사항 상세" width="200" />  
        </td>
        <td align="center" width="200">
            <h5>아이디 찾기</h5>
            <img src="./images/teacher_id_find.png" alt="교수 아이디 찾기" width="200" />  
        </td>
        <td align="center" width="200">
            <h5>비밀번호 찾기</h5>
            <img src="./images/teacher_password_find.png" alt="교수 비밀번호 찾기" width="200" />  
        </td>
        <td align="center" width="200">
            <h5>개인 정보 수정 비밀번호 확인</h5>
            <img src="./images/teacher_info_edit_password.png" alt="교수 개인 정보 수정 비밀번호 확인" width="200" />  
        </td>
        <td align="center" width="200">
            <h5>개인 정보 수정</h5>
            <img src="./images/teacher_info_edit.png" alt="교수 개인 정보 수정" width="200" />  
        </td>
        <td align="center" width="200">
            <h5>과제별 채점 리스트</h5>
            <img src="./images/teacher_submission_grading_list.png" alt="과제별 채점 리스트" width="200" />  
        </td>
        <td align="center" width="200">
            <h5>과제 채점 및 피드백 작성</h5>
            <img src="./images/teacher_submission_grading_feedback.png" alt="과제 채점 및 피드백 작성" width="200" />  
        </td>
    </tr>
    <tr>
      <td align="center">
        <div>✔ 공지사항 목록을 배경색만 녹색으로 보여줍니다.</div>
      </td>
      <td align="center">
        <div>✔ 공지사항 상세 페이지를 배경색만 녹색으로 보여줍니다.</div>
      </td>
      <td align="center">
        <div>✔ 아이디 찾기 페이지입니다.</div>
      </td>
      <td align="center">
        <div>✔ 비밀번호 찾기 페이지입니다.</div>
      </td>
      <td align="center">
        <div>✔ 개인 정보 변경 전 비밀번호 확인 페이지입니다.</div>
      </td>
      <td align="center">
        <div>✔ 개인 정보 수정하는 페이지입니다.</div>
      </td>
      <td align="center">
        <div>✔ 만든 과제에 대해 채점 현황을 보여주는 페이지입니다.</div>
      </td>
      <td align="center">
        <div>✔ 개인별 과제 점수 및 피드백을 작성하는 페이지입니다.</div>
      </td>
    </tr>
</table>


### 4. 학생

<table>
    <tr>
        <td align="center" width="200">
            <h5>수강 신청 페이지</h5>
            <img src="./images/student_subject_list.png" alt="수강 신청 리스트" width="200" />  
        </td>
        <td align="center" width="200">
            <h5>과제 관리 목록</h5>
            <img src="./images/student_submission_list.png" alt="과제 관리 리스트" width="200" />  
        </td>
        <td align="center" width="200">
            <h5>과제 작성</h5>
            <img src="./images/student_submission_write.png" alt="과제 작성" width="200" />  
        </td>
        <td align="center" width="200">
            <h5>과제 상세 조회</h5>
            <img src="./images/student_submission_view.png" alt="과제 상세 조회" width="200" />  
        </td>
        <td align="center" width="200">
            <h5>제출한 과제 상세 조회</h5>
            <img src="./images/student_my_submission_view.png" alt="제출한 과제 상세 조회" width="200" />  
        </td>
    </tr>
    <tr>
      <td align="center">
        <div>✔ 수강 신청 및 취소 페이지입니다.</div>
        <div>✔ 신청기간에 날짜에 포함되어 있어야 수강 신청 가능합니다.</div>
      </td>
      <td align="center">
        <div>✔ 내가 신청한 과목의 모든 과제현황을 보여주는 페이지입니다.</div>
      </td>
      <td align="center">
        <div>✔ 내가 선택한 과제 제출을 위해 작성하는 페이지입니다.</div>
      </td>
      <td align="center">
        <div>✔ 교수가 작성한 과제의 상세 내용을 볼 수 있는 페이지입니다.</div>
      </td>
      <td align="center">
        <div>✔ 내가 제출한 과제의 상세 내용을 볼 수 있는 페이지입니다.</div>
        <div>✔ 프론트엔드, 백엔드에서 수정 및 삭제 기능을 마감 기한에 맞게 가능 또는 불가능하게 구현하였습니다.</div>
      </td>
    </tr>
</table>
<table>
    <tr>
        <td align="center" width="200">
            <h5>공지사항 목록</h5>
            <img src="./images/student_notice_list.png" alt="학생 공지사항 목록" width="200" />  
        </td>
        <td align="center" width="200">
            <h5>공지사항 상세</h5>
            <img src="./images/student_notice_view.png" alt="학생 공지사항 상세" width="200" />  
        </td>
        <td align="center" width="200">
            <h5>아이디 찾기</h5>
            <img src="./images/student_id_find.png" alt="학생 아이디 찾기" width="200" />  
        </td>
        <td align="center" width="200">
            <h5>비밀번호 찾기</h5>
            <img src="./images/student_password_find.png" alt="학생 비밀번호 찾기" width="200" />  
        </td>
        <td align="center" width="200">
            <h5>개인 정보 수정 비밀번호 확인</h5>
            <img src="./images/student_info_edit_password.png" alt="학생 개인 정보 수정 비밀번호 확인" width="200" />  
        </td>
        <td align="center" width="200">
            <h5>개인 정보 수정</h5>
            <img src="./images/student_info_edit.png" alt="학생 개인 정보 수정" width="200" />  
        </td>
    </tr>
    <tr>
      <td align="center">
        <div>✔ 공지사항 목록을 배경색만 남색으로 보여줍니다.</div>
      </td>
      <td align="center">
        <div>✔ 공지사항 상세 페이지를 배경색만 녹색으로 보여줍니다.</div>
      </td>
      <td align="center">
        <div>✔ 아이디 찾기 페이지입니다.</div>
      </td>
      <td align="center">
        <div>✔ 비밀번호 찾기 페이지입니다.</div>
      </td>
      <td align="center">
        <div>✔ 개인 정보 변경 전 비밀번호 확인 페이지입니다.</div>
      </td>
      <td align="center">
        <div>✔ 개인 정보 수정하는 페이지입니다.</div>
      </td>
    </tr>
</table>
<div align="right"><a href="#tableContents">목차로 이동</a></div>

<br/>

### ✔ 프로젝트 결과물

---

<!-- - [포팅메뉴얼] -->
<!-- - [발표자료] -->
- [중간발표자료](./ppt/중간발표_eLMS.pdf)
- [최종발표자료](./ppt/최종발표_eLMS.pdf)


<!------- 팀원 소개 시작 -------->

## 👥 팀원 소개

<a name="developers"></a>
<table>
    <tr>
        <td align="center" width="200">
            <h5>Name</h5>
        </td>
        <td align="center" width="200">
            <h5>진선용</h5>
        </td>
        <td align="center" width="200">
            <h5>박세연</h5>
        </td>
        <td align="center" width="200">
            <h5>박윤희</h5>
        </td>
        <td align="center" width="200">
            <h5>박정희</h5>
        </td>
        <td align="center" width="200">
            <h5>백세원</h5>
        </td>
        <td align="center" width="200">
            <h5>정정원</h5>
        </td>
    </tr>
    <tr>
        <td align="center" width="200">
            <h5>역할</h5>
        </td>
        <td align="center" width="200">
            <h5>풀스택</h5>
        </td>
        <td align="center" width="200">
            <h5>프론트엔드</h5>
        </td>
        <td align="center" width="200">
            <h5>풀스택</h5>
        </td>
        <td align="center" width="200">
            <h5>프론트엔드</h5>
        </td>
        <td align="center" width="200">
            <h5>풀스택</h5>
        </td>
        <td align="center" width="200">
            <h5>프론트엔드</h5>
        </td>
    </tr>
</table>

<div align="right"><a href="#tableContents">목차로 이동</a></div>
