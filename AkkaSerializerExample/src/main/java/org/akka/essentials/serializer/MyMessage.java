package org.akka.essentials.serializer;

public class MyMessage {

	private String name;
	private Integer age;
	private String address;

	public MyMessage(String _name, Integer _age, String _address) {
		name = _name;
		age = _age;
		address = _address;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String toString() {
		return new StringBuffer().append(name).append(",").append(age)
				.append(",").append(address).toString();
	}

}
