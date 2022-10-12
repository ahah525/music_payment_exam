# 음원 결제 사이트 예제
>Skills
- Spring Boot, JPA, Thymeleaf
- Toss Payments 사용
>기능 소개
- 음원 업로드 기능
  - 로그인한 회원만 가능
- 음원 상세조회 기능
  - 음원 등록한 회원(본인)만 가능
- 음원 수정 기능
  - 음원 등록한 회원(본인)만 가능
- 등록된 음원 상품화 기능
  - 색상/사이즈/개수/재고/배송 개념X
- 음원 구매 기능
  - 본인이 만든 음원상품은 장바구니에 담을 수 없음
  - 3가지 결제 방식 지원
    - 예치금 전액 결제(PG 결제 skip)
    - Toss Payments 카드 전액 결제(예치금 충전 기록 보존)
    - 예치금 + Toss Payments 카드 결제(혼합 방식)
- 음원 소유자 정산 기능
  - 예치금 기반