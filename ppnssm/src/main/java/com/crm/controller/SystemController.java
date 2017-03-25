/**
 * 
 */
package com.crm.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.crm.model.SysMenu;
import com.crm.model.User;
import com.crm.model.easyui.Json;
import com.crm.model.easyui.Tree;
import com.crm.service.UserService;
import com.crm.util.RequestUtil;
import com.crm.util.UserCookieUtil;
import com.crm.util.common.Const;
import com.sojson.core.shiro.token.manager.TokenManager;

/**
 * @author zh
 * 2014-7-26
 */
@Controller
public class SystemController extends BaseController {
	private final Logger log = LoggerFactory.getLogger(SystemController.class);
	@Resource
	private UserService userService;
	
	/*@RequestMapping(value = "/",method = RequestMethod.GET)
	public String home() {
		log.info("返回首页！");
		return "login.htm";
	}*/
	
    /*@RequestMapping(value = "/login",method = RequestMethod.POST)
    public String login(HttpServletRequest request,HttpServletResponse response,
    		@RequestParam String loginname, @RequestParam String password, 
    		@RequestParam String code,@RequestParam String autologinch) throws Exception{
    	
		if (code.toLowerCase().equals(request.getSession().getAttribute("RANDOMCODE").toString().toLowerCase())){
			User user = userService.findUserByName(loginname);
			if (user == null) {
				log.info("登陆用户名不存在");  
	    		request.getSession().setAttribute("message", "用户名不存在，请重新登录");
	    		return "redirect:juum/login.htm"; 
			}else {
				if (user.getPassword().equals(password)) {
					
					if(autologinch!=null && autologinch.equals("Y")){ // 判断是否要添加到cookie中
						// 保存用户信息到cookie
						UserCookieUtil.saveCookie(user, response);
					}
					
					// 保存用信息到session
					request.getSession().setAttribute(Const.SESSION_USER, user);  
	        		return "redirect:" + RequestUtil.retrieveSavedRequest();//跳转至访问页面
					
				}else {
					log.info("登陆密码错误");  
	        		request.getSession().setAttribute("message", "用户名密码错误，请重新登录");
	        		return "redirect:juum/login.htm"; 
				}
			}
		}else {
			request.getSession().setAttribute("message", "验证码错误，请重新输入");
    		return "redirect:juum/login.htm"; 
		}
    }*/
    
    @RequestMapping(value = "/login",method=RequestMethod.GET)
    public String login(){
    	return "login"; 
    }
    
    @ResponseBody
    @RequestMapping(value = "/loginjson",method = RequestMethod.POST)
    public Json login2(HttpServletRequest request,HttpServletResponse response,
    		@RequestParam String loginname, @RequestParam String password, 
    		@RequestParam String code,@RequestParam String autologinch) throws Exception{
    	Json result = new Json();
		if (code.toLowerCase().equals(request.getSession().getAttribute("RANDOMCODE").toString().toLowerCase())){
			User user = userService.findUserByName(loginname);
			if (user == null) {
				log.info("登陆用户名不存在"); 
				result.setSuccess(false);
				result.setMsg("登陆用户名不存在！");
	    		request.getSession().setAttribute("message", "用户名不存在，请重新登录");
	    		return result; 
			}else {
				if (user.getPassword().equals(password)) {
					
					if(autologinch!=null && autologinch.equals("Y")){ // 判断是否要添加到cookie中
						// 保存用户信息到cookie
						UserCookieUtil.saveCookie(user, response);
					}
					
					// 保存用信息到session
					request.getSession().setAttribute(Const.SESSION_USER, user);
					result.setSuccess(true);
	        		return result;
					
				}else {
					log.info("登陆密码错误");
					result.setSuccess(false);
					result.setMsg("登陆密码错误！");
	        		request.getSession().setAttribute("message", "用户名密码错误，请重新登录");
	        		return result; 
				}
			}
		}else {
			request.getSession().setAttribute("message", "验证码错误，请重新输入");
			result.setSuccess(false);
			result.setMsg("验证码错误，请重新输入！");
    		return result; 
		}
    }
        
	/**
	 * 用户注销
	 * @return
	 */
	@RequestMapping(value="/logout")
	public String logout(HttpSession session,HttpServletResponse response){
		session.removeAttribute(Const.SESSION_USER);
		UserCookieUtil.clearCookie(response);
		return "redirect:/";
	}
	
    /**
     * 测试缓存
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value="/get/{id}", method = RequestMethod.GET)  
    public String get(@PathVariable int id, Model model){  
        String username = userService.getUsernameById(id);  
        model.addAttribute("username", username);  
        return "getUsername";  
    } 
    
    /**
     * 获取菜单栏(easyUI Tree)
     * @param id
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/getMenu", method = RequestMethod.POST)  
    public List<Tree> getMenu(HttpSession session){ 
    	User user =  (User)session.getAttribute(Const.SESSION_USER);
    	List<SysMenu> menuList;
    	if(user!=null){
    		menuList = userService.getMenu(user.getId());
    	}else{
    		menuList = userService.getMenu(1);
    	}
    	
    	List<Tree> treeList = createTree(menuList);
    	/*User user =  (User)session.getAttribute(Const.SESSION_USER); 
    	List<SysMenu> menuList = userService.getMenu(1);
    	List<Tree> treeList = new ArrayList<Tree>();
        for (SysMenu menu : menuList) {
			Tree node = new Tree();
			node.setId(menu.getId());
			node.setPid(menu.getParentid());
			node.setText(menu.getName());
			node.setIconCls(menu.getIconCls());
			
			if(menu.getParentid()!=0){	// 有父节点
				node.setPid(menu.getParentid());
			}
			if(menu.getCountChildrens() > 0){	//有子节点
				node.setState("closed");
			}
			Map<String, Object> attr = new HashMap<String, Object>();
			attr.put("url", menu.getUrl());
			node.setAttributes(attr);
			treeList.add(node);
			List<Tree> treeList = getTreeNode(0);
        }*/
    	
    	return treeList;
    }
       
    /**
     * 创建一颗树，以List返回
     * @param list 原始数据列表
     * @return 树
     */
   private  List<Tree> createTree(List<SysMenu> list) {
	   List<Tree> treeList = new ArrayList<Tree>();
       for (SysMenu menu : list) {
           if (menu.getParentid() == 0 ) { //有父节点
        	   		
        	   Tree rootObj = createBranch(list, menu);
        	   treeList.add(rootObj);
           }
       }

       return treeList;
   }

   /**
    * 递归创建分支节点Json对象
    * @param list 创建树的原始数据
    * @param currentNode 当前节点
    * @return 当前节点与该节点的子节点json对象
    */
   private Tree createBranch(List<SysMenu> list, SysMenu currentNode) {
	   
	   Tree currentObj = new Tree();
	   currentObj.setId(currentNode.getId());
	   currentObj.setText(currentNode.getName());
	   if(currentNode.getCountChildrens() > 0){	//有子节点
		   currentObj.setState("open");
	   }
	   Map<String, Object> attr = new HashMap<String, Object>();
	   attr.put("url", currentNode.getUrl());
	   currentObj.setAttributes(attr);
		
	   List<Tree> treeList = new ArrayList<Tree>();

       /*
        * 循环遍历原始数据列表，判断列表中某对象的父id值是否等于当前节点的id值，
        * 如果相等，进入递归创建新节点的子节点，直至无子节点时，返回节点，并将该
        * 节点放入当前节点的子节点列表中
        */
       for (SysMenu newNode : list) {
           if ((newNode.getParentid() != 0) && (newNode.getParentid().compareTo(currentNode.getId()) == 0)) {
        	   Tree childObj = createBranch(list, newNode);
        	   treeList.add(childObj);
           }
       }
       /*
        * 判断当前子节点是否为空，不为空将子节点加入children字段中
        */
       if(treeList!=null){
    	   currentObj.setChildren(treeList);
       }


       return currentObj;
   }
    
}
