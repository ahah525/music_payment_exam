# 음원 결제 사이트 예제
## Skills
- Spring Boot, JPA, Thymeleaf
- Toss Payments 사용
## 기능 소개
- 음원 기능
  - 음원 업로드 기능
    - 로그인한 회원만 가능
  - 음원 리스트 조회 기능
    - 음원 등록한 회원(본인)만 가능
  - 음원 상세조회 기능
    - 음원 등록한 회원(본인)만 가능
  - 음원 수정 기능
    - 음원 등록한 회원(본인)만 가능
- 음원 상품 등록 기능
  - 업로드한 본인의 음원 상품화 기능
  - 음원 리스트 페이지에서 상품등록 버튼을 통해 상품 등록 페이지로 이동
  - 같은 음원에 대한 상품화 여러번 가능
  - 색상/사이즈/개수/재고/배송 개념X
- 장바구니 기능
  - 장바구니 품목 조회(본인만 가능)
  - 장바구니 품목 선택(전체 선택 포함) 삭제 기능(본인만 가능)
  - 본인이 만든 음원상품은 장바구니 담기 불가
- 음원 주문 기능
  - 장바구니 페이지의 전체 주문하기를 통해 주문 가능(선택 주문 미지원)
  - 주문 성공시, 주문 상세조회 페이지로 리다이렉트 
- 음원 구매 기능
  - 주문 상세조회 페이지에서 결제 가능(미결제 상태일 때만 결제 가능)
  - 3가지 결제 방식 지원
    - 예치금 전액 결제(PG 결제 skip)
    - Toss Payments 카드 전액 결제(예치금 충전 기록 보존)
    - 예치금 + Toss Payments 카드 결제(혼합 방식)
- 음원 소유자 정산 기능
  - 월별 정산 데이터 생성
  - 정산 데이터 조회
  - 단건 정산, 선택(전체 선택 포함) 정산 지원
  - 예치금 기반 정산 처리

## 관리자 페이지
- 관리자(ADMIN 권한가진 회원) : user1
- 관리자만 접근 가능 
### 1. 정산 기능
- 관리자가 월별 데이터 정산
  1) 정산 데이터 생성 작업
     1) 해당 날짜 범위의 주문 품목(OrderItem) 데이터 조회
     2) 주문 품목 데이터(OrderItem) -> 정산 데이터(RebateOrderItem) 변환
     3) 정산 데이터 생성 및 DB에 저장
  2) 정산 데이터로부터 정산 수행(예치금 작업)
     - 전액 환불건, 정산 완료건은 정산 불가
     - 정산 금액 = 판매가 - 결제 대행사 수수료 - 도매가
     - 음원소유자(판매자)에게 예치금으로 정산금액 지급

## 결제 관련 개념 정리
>PG 
  - 전자결제 대행사(KG 이니시스, 토스 페이먼츠, NHN한국사이버결제)
  - 스마트폰 또는 인터넷으로 결제를 진행할 때, 결제정보를 확인 및 승인해주는 솔루션
  
>주문 품목 가격 관련 필드

| 필드(컬럼)          | 용어         | 설명                                                |
|-----------------|------------|---------------------------------------------------|
| pay_date        | 권장 소비자가    | 실제 상품에 표시되는 가격<br/>(기본 할인되기 전 원래 가격)              |
| price           | 판매가        | 실제 소비자에게 판매되는 가격<br/>(권장 소비자가에서 기본 할인이 붙은 가격)     |
| wholesale_price | 도매가        | 사입처에서 상품을 구매한 가격                                  |
| pg_fee          | 결제 대행사 수수료 | PG사에게 내는 결제금액의 일정수수료                              |
| pay_price       | 결제 금액      | 사용자가 결제해야하는 최초 금액(예치금 사용전)                        |
| refund_price    | 환불 금액      | 사용자가 환불받을 금액                                      |
