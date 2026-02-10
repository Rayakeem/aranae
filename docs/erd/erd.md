# ERD (초안)

## 1. 목표
아라내는 팬이 연예인 패션 정보를 **등록/수정 제안(EditProposal)**하고, 관리자가 **승인/반려**하여 실제 데이터에 반영하는 서비스다.  
따라서 DB는 다음을 보장해야 한다.

- 제안(PENDING)과 실제 데이터(celebrities/items/celebrity_items)를 분리한다.
- 승인/반려가 발생하면 **누가/언제/무슨 결정**을 했는지 기록이 남는다.
- 중복 데이터/중복 제안을 방지한다.

---

## 2. 엔티티 요약

### users
- 사용자(팬/관리자)
- `role`: USER / ADMIN

### celebrities
- 연예인 정보
- `group_name`: 아이돌 그룹명(선택)

### items
- 패션 아이템 정보
- `unique_key`: 아이템 중복 판단용 키(아래 “중복 방지 전략” 참고)

### celebrity_items
- 연예인-아이템 연결(N:M)
- 착장 메타 정보(채널/콘텐츠 종류/프로그램명 등)를 포함

### edit_proposals
- 팬이 올리는 등록/수정 제안 데이터
- `proposal_type`: CREATE / UPDATE
- `target_type`: CELEBRITY / ITEM / CELEBRITY_ITEM
- `status`: PENDING / APPROVED / REJECTED
- `proposed_data`: 제안 내용(JSON)
- `dedup_key`: 대기중(PENDING) 중복 제안 방지 키

### approval_histories
- 관리자의 승인/반려 기록(감사 로그)
- 제안 1건당 승인/반려는 1번만 허용

---

## 3. 핵심 제약조건(Constraints)

### 3.1 유니크(중복 방지)
- `users.email` UNIQUE
- `celebrities.name` UNIQUE
- `items.unique_key` UNIQUE  
  - 동일 아이템을 “다른 id로” 중복 생성하는 것을 방지
- `celebrity_items(celebrity_id, item_id)` UNIQUE  
  - 동일 연예인-아이템 연결의 중복 생성 방지(MVP에서는 대표 착장 1개만 유지)
- `approval_histories.edit_proposal_id` UNIQUE  
  - 제안 1건당 승인/반려는 단 1회만 가능

### 3.2 NOT NULL
- 모든 PK/FK 및 상태값은 NOT NULL을 기본으로 한다.
- `edit_proposals.proposed_data`, `edit_proposals.dedup_key`는 NOT NULL

---

## 4. 중복 방지 전략 (중요)

### 4.1 아이템 중복 방지: items.unique_key
DB의 PK(id)만으로는 “같은 아이템인지” 판단할 수 없다.  
따라서 아이템을 식별할 수 있는 `unique_key`를 생성하여 UNIQUE 제약으로 중복을 방지한다.

- 예시 생성 규칙(앱 레벨):
  - `lower(brand) + "|" + normalize(name) + "|" + lower(category)`
- 예시:
  - `nike|air-force-1-low-white|shoes`

### 4.2 대기중 중복 제안 방지: edit_proposals.dedup_key
동일한 내용의 제안이 PENDING 상태로 여러 개 쌓이지 않도록 `dedup_key`를 사용한다.

- 예시:
  - ITEM CREATE: `ITEM|CREATE|nike|air-force-1-low-white|shoes`
  - CELEBRITY_ITEM CREATE: `CELEBRITY_ITEM|CREATE|celebrity=3|item=10`

서버는 제안 생성 시 `status=PENDING` + `dedup_key`가 이미 존재하면 생성 요청을 거절한다.

---

## 5. 승인/반영 규칙 (Workflow)

### 5.1 제안 생성
- 유저는 실제 테이블(celebrities/items/celebrity_items)을 직접 수정할 수 없다.
- 모든 등록/수정은 `edit_proposals`에 저장된다.
  - 최초 등록도 `proposal_type=CREATE`로 처리한다.

### 5.2 승인/반려 처리 (트랜잭션)
관리자가 제안을 승인/반려할 때 서버는 하나의 트랜잭션에서 다음을 수행한다.

1) `edit_proposals.status`를 `PENDING → APPROVED/REJECTED`로 변경  
2) `approval_histories`에 기록 생성(제안당 1회만)  
3) 승인(APPROVED)인 경우에만 `proposed_data`를 실제 테이블에 반영  
   - target_type에 따라 분기:
     - ITEM + CREATE/UPDATE → items insert/update
     - CELEBRITY + CREATE/UPDATE → celebrities insert/update
     - CELEBRITY_ITEM + CREATE → celebrity_items insert

실패 시 롤백하여 상태/로그/실데이터 불일치가 발생하지 않도록 한다.

---

## 6. proposed_data 예시

### 6.1 ITEM CREATE 예시
```json
{
  "name": "Air Force 1 Low White",
  "brand": "Nike",
  "category": "Shoes",
  "price": 129000,
  "image_key": "items/nike/af1-white.png",
  "purchase_url": "https://example.com/item/123"
}