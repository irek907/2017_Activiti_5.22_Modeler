package org.activiti.rest.editor.main;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
@RestController
public class CreateController {
	  @Autowired
	  private RepositoryService repositoryService;
	@RequestMapping(value = "/create", method = RequestMethod.POST)  
	   public void create(@RequestParam("name") String name, @RequestParam("key") String key, @RequestParam("description") String description,  
	                      HttpServletRequest request, HttpServletResponse response) {  
	      Map<String, String[]> parameterMap = request.getParameterMap();  
	      
	    try {  
	           ObjectMapper objectMapper = new ObjectMapper();  
	           ObjectNode editorNode = objectMapper.createObjectNode();  
	           editorNode.put("id", "canvas");  
	           editorNode.put("resourceId", "canvas");  
	           ObjectNode stencilSetNode = objectMapper.createObjectNode();  
	           stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");  
	           editorNode.put("stencilset", stencilSetNode);  
	           Model modelData = repositoryService.newModel();  
	  
	           ObjectNode modelObjectNode = objectMapper.createObjectNode();  
	           modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, name);  
	           modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);  
	           description = StringUtils.defaultString(description);  
	           modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);  
	           modelData.setMetaInfo(modelObjectNode.toString());  
	           modelData.setName(name);  
	           modelData.setKey(StringUtils.defaultString(key));  
	  
	           repositoryService.saveModel(modelData);  
	           repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes("utf-8"));  
	             
	          //request.setAttribute("modelId", o);  
	             
	           response.sendRedirect(request.getContextPath() + "/modeler.html?modelId=" + modelData.getId());  
	       } catch (Exception e) {  
	           //logger.error("创建模型失败：", e);  
	       }  
	   }  
}
