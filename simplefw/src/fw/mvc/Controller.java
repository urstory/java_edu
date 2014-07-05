package fw.mvc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
 @Target :  어노테이션(Annotation)을 적용할 대상을 지정함
            value값으로는 ElementType에 enum상수인 다음의 값들을 사용할수있다.
            즉 어디에 어노테이션을 넣을 수 있는지를 서술하는데 field, method, class가 정의된 곳에 어노테이션을 넣을 수 있다.

	CONSTRUCTOR : 생성자
	FIELD : enum상수를 포함한 필드
	LOCAL_VARIABLE : 지역 변수
	METHOD : 메소드
	PACKAGE : 패키지
	PARAMETER : 파라미터
	TYPE : Class, Interface, Enumeration에 어노테이션을 적용
 
 @Retention : 어노테이션이 얼마나 오랫동안 유지되는지에 대해, JVM이 어떻게 사용자 어노테이션을 다루어야 하는지를 서술
              Annotation 정보가 언제까지 유지될지 지정, 어노테이션 정보가 보관되는 기간
              value값으로는 RententionPolicy에 enum상수인 다음의 값들을 사용할수있음

	SOURCE : 어노테이션이 컴파일 타임시 버려진다는 것을 의미, 클래스파일은 애노테이션을 가지지 못함
	CLASS : 어노테이션이 생성된 클래스 파일에서 나타날 것이라는 것을 의미, 런타임시에는 이 어노테이션을 이용하지 못함
	RUNTIME : 소스, 클래스, 실행시에 사용됨

 @Documented : Annotation을 JavaDoc에 포함함
 @Inherited :  부모 Annotation을 상속받음, 기본적으로 어노테이션은 상속되지 않는다. 
               따라서 상속을 원한다면, 어노테이션을 @Inherited 해야 하며, 사용자 어노테이션에 놓여야 하며 클래스에만 효과가 있다.
*/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Controller {

}
