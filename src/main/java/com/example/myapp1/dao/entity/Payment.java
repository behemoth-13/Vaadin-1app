package com.example.myapp1.dao.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Embeddable
public class Payment{

	@Max(value = 100, message = "100% is max deposit")
	@Min(value = 0, message = "0% is min deposit")
	@Column(name = "GUARANTY_FEE")
	private Integer deposit;
	
	public Payment() {
	}
	
	public Payment(Integer deposit) {
		this.deposit = deposit;
	}

	public Integer getDeposit() {
		return deposit;
	}

	public void setDeposit(Integer deposit) {
		this.deposit = deposit;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((deposit == null) ? 0 : deposit.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Payment other = (Payment) obj;
		if (deposit == null) {
			if (other.deposit != null)
				return false;
		} else if (!deposit.equals(other.deposit))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Payment [deposit=" + deposit + "]";
	}
}
