package jpabook.jpashop.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class Address {

    private String city;
    private String street;
    private String zipcode;
    
    // 값 타잆은 변경 불가능 하게 설계.
    // 그러므로 setter 는 사용 x 
    // 생성자로 최초 호출시에만 사용하도록
    // 기본생성자는 JPA 내부 기능 (리플렉션, 프록시 등) 을 사용하기 위해서 
    // JPA 가 protected 까진 허용 해줌
    protected Address() {
    }
    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }

}
