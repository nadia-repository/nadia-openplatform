package com.nadia.openplatfrom.isv.account.properties;

import lombok.Data;
import org.springframework.stereotype.Component;


@Data
public class AccountProperties {
	private int maxLoginFailTimes = 5;

}
