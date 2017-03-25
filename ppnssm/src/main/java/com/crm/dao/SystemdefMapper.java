package com.crm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.crm.model.Systemdef;
import com.crm.model.easyui.PageHelper;

public interface SystemdefMapper {
	
	public List<Systemdef> getAll(PageHelper page);
	public List<Systemdef> getAllByParentId(@Param("sysPid")int sysPid);

	public Systemdef getSystemdefById(Integer id);
	
	public void insertSystemdef(Systemdef systemdef);
	
	public void updateSystemdef(Systemdef systemdef);
	
	public void deleteSystemdefById(Integer id);
	
}
