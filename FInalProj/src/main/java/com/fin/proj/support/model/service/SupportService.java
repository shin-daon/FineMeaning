package com.fin.proj.support.model.service;

import com.fin.proj.support.model.vo.Support;
import com.fin.proj.support.model.vo.SupportDetail;

public interface SupportService {

	int supportApply(Support s);

	int insertSupportDetail(SupportDetail supportDetail);


}
