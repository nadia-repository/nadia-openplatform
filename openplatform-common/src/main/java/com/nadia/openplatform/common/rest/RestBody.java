package com.nadia.openplatform.common.rest;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class RestBody<T> extends RestHeader{
	private T data;
}
