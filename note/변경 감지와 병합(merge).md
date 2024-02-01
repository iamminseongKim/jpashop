> 참고 : 정말 중요한 내용이니 꼭! 완벽히 숙지해야 한다

**준영속 엔티티?**
영속성 컨텍스트가 더는 관리하지 않는 엔티티를 말한다.
(여기서는 `itemService.saveItem(book)`)에서 수정을 시도하는 `Book`객체다. `Book` 객체는 이미 DB에 한번 저장되어서 식별자가 존재한다. 이렇게 임의로 만들어낸 엔티티도 기존 식별자를 가지고 있으면 준영속 엔티티로 볼 수 있다.)

**준영속엔티티를 수정하는 2가지 방법**
- 변경 감지 기능 사용
- 병합(`merge`) 사용

### 변경 감지 기능 사용

```
@Transactional  
public void updateItem(Long itemId, Book param) {  
    Item findItem = itemRepository.findOne(itemId);  
    findItem.setPrice(param.getPrice());  
    findItem.setName(param.getName());  
    ...
    ...
}
```
이건 New 로 book (item) 을 생성한게 아니라 jpa로 엔티티를 가져왔기 때문에 
set으로 하고 따로 save 를 안해도 변경 감지로 인해 트랜잭션 커밋 시점에 변경감지가 일어난다.

### 병합 사용
병합은 준영속 상태의 엔티티를 영속 상태로 변경할 때 사용하는 기능이다.

```
@Transactional
void update(Item item) {
	Item mergeItem = em.merge(item)
}
```
![](https://i.imgur.com/tCqSMSJ.png)
그니깐 item이 영속성이 아니라 mergeItem 이 영속성 엔티티이다.


**병합 동작 방식**
1. `merge()` 를 실행한다.
2. 파라미터로 넘어온 준영속 엔티티의 식별자 값으로 1차 캐시에서 엔티티를 조회한다. 
	- 만약 1차 캐시에 엔티티가 없으면 데이터 베이스에서 엔티티를 조회하고, 1차 캐시에 저장한다.
3. 조회한 영속 엔티티(`mergeMember`)에 `member`엔티티의 값을 채워 넣는다. (member 엔티티의 모든 값을 `mergeMember`에 밀어 넣는다. 이때 `mergeMember`의 "회원1" 이라는 이름이 "회원명변경"으로 바뀐다.)
4. 영속 상태인 mergeMember를 반환한다.

**참고 : 병합 시 동작 방식을 간단히 정리**
1. 준영속 엔티티의 식별자 값으로 영속 엔티티를 조회한다.
2. 영속 엔티티의 값을 준영속 엔티티의 값으로 모두 교체 한다 (병합)
3. 트랜잭션 커밋 시점에 변경 감지 기능이 동작해서 데이터베이스에 UPDATE SQL 실행

> 주의 : 변경 감지 기능을 사용하면 원하는 속성만 선택해서 변경할 수 있지만, 병합을 사용하면 모든 속성이 변경된다. 병합 시 값이 없으면 `null` 로 업데이트 할 위험도 있다. (병합은 모든 필드를 교체하기 때문에)

