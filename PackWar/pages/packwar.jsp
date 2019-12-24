<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<% String path = request.getContextPath(); %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>EasyPackWar</title>
	<meta http-equiv="description" content="This is my page">
		<link rel="stylesheet" type="text/css" href="<%=path%>/scripts/css/app.css">
		<link rel="stylesheet" type="text/css" href="<%=path%>/scripts/css/easyui/easyui.css">
		<link rel="stylesheet" type="text/css" href="<%=path%>/scripts/css/easyui/icon.css">
		<link rel="stylesheet" type="text/css" href="<%=path%>/scripts/css/bootstrap.css">
		<link rel="stylesheet" type="text/css" href="<%=path%>/scripts/css/bootstrap-theme.css">
		<link rel="stylesheet" type="text/css" href="<%=path%>/scripts/css/toastr/toastr.scss">
		<link rel="stylesheet" type="text/css" href="<%=path%>/scripts/css/icheck/blue.css">
		<link rel="stylesheet" type="text/css" href="<%=path%>/scripts/css/icheck/icheck-bootstrap.css">
		<link rel="stylesheet" type="text/css" href="<%=path%>/scripts/css/bootstrap-datetimepicker.css">
		
		<script type="text/javascript" src="<%=path%>/scripts/js/jquery-1.11.3.min.js"></script>
		<script type="text/javascript" src="<%=path%>/scripts/js/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="<%=path%>/scripts/js/bootstrap.js"></script>
		<script type="text/javascript" src="<%=path%>/scripts/js/toastr.js"></script>
		<script type="text/javascript" src="<%=path%>/scripts/js/bootbox.min.js"></script>
		<script type="text/javascript" src="<%=path%>/scripts/js/icheck.js"></script>
		<script type="text/javascript" src="<%=path%>/scripts/js/jquery.blockUI.js"></script>
		<script type="text/javascript" src="<%=path%>/scripts/js/bootstrap-datetimepicker.js"></script>
		<script type="text/javascript" src="<%=path%>/scripts/js/locales/bootstrap-datetimepicker.zh-CN.js"></script>
		<script type="text/javascript" src="<%=path%>/scripts/js/serializeForm.js"></script>
  </head>
  
  <body>
  <jsp:include page="header.jsp"/>
  <div class="all font_c" width="100%">
		<input id="local_addr_nm" name="local_addr_nm" type="hidden" value="">
		<input id="svn_addr_nm" name="svn_addr_nm" type="hidden" value="">
		<input id="EuiTree_addr" name="EuiTree_addr" type="hidden" value="">
		<input id="EuiTreeTwo_addr" name="EuiTreeTwo_addr" type="hidden" value="">
		<input id="acc_name" name="acc_name" type="hidden" value="${name }">
		<input id="acc_password" name="acc_password" type="hidden" value="${password }">
		<input id="account_flag" name="account_flag" type="hidden" value="${account_flag }">
		<table class="table table-bordered">
			<tbody>
				<tr>
					<th width="10%" style="text-align:right">
						<span style="color: red;">项目目录：</span>
					</th>
					<td width="40%">
						<div class="input-group">
							<textarea id="local_addr" name="local_addr" class="form-control" rows="2" style="width:100%;font-weight:bold;" 
								onkeyup="getProDirs('local_addr')" >${local_addr }</textarea>
							<div class="input-group-btn" >
								<button type="button" class="btn btn-default"  title="上层目录"
									onclick="deleteUri('local_addr')">&#8593;
								</button>
							</div>
							<div class="input-group-btn">
								<button id="add_local_addr" type="button" class="btn btn-default dropdown-toggle" title="下层目录"
									data-toggle="dropdown" >&#8595;
								</button>
								<ul id="local_pro" class="dropdown-menu pull-right">
									<c:forEach items="${local_addr_list }" var="pro">
										<li><a href="javascript:addUri('${pro }', 'local_addr');">${pro }</a></li>
									</c:forEach>
								</ul>
							</div>
						</div>
 					</td>
 					<th width="10%" style="text-align:right">
						<span style="color: red;">升级文件目录：</span>
					</th>
					<td width="40%">
						<div class="input-group">
							<textarea id="pack_addr" name="pack_addr" class="form-control" rows="2" style="width:100%;font-weight:bold;" 
								onkeyup="getProDirs('pack_addr')" >${pack_addr }</textarea>
							<div class="input-group-btn">
								<button type="button" class="btn btn-default" title="上层目录"
									onclick="deleteUri('pack_addr')">&#8593;
								</button>
							</div>
							<div class="input-group-btn">
								<button id="add_pack_addr" type="button" class="btn btn-default dropdown-toggle" 
									data-toggle="dropdown" title="下层目录" >&#8595;</button>
								<ul id="pack_pro" class="dropdown-menu pull-right">
									<c:forEach items="${pack_addr_list }" var="pro">
										<li><a href="javascript:addUri('${pro }', 'pack_addr');">${pro }</a></li>
									</c:forEach>
								</ul>
							</div>
						</div>
					</td>
				</tr>
				<tr>
					<th style="text-align:right">
						设置文件名称：
					</th>
					<td style="font-weight:bold;">
						<div class="input-group">
							<input id="webRootName" class="form-control" style="width:39%" readonly="readonly" placeholder="自动生成，无法手动修改。"	/>
							<input id="update_type" class="form-control" style="width:16%;color:red;" onclick="changeType(this)" value="SIT" />
							<input id="time_str" class="form-control" style="width:25%" />
							<select id="num_str" class="form-control" style="width:20%;color:red;" value="">
								<option>01</option>
								<option>02</option>
								<option>03</option>
								<option>04</option>
								<option>05</option>
								<option>06</option>
								<option>07</option>
								<option>08</option>
								<option>09</option>
								<option>10</option>
								<option>11</option>
								<option>12</option>
								<option>13</option>
								<option>14</option>
								<option>15</option>
								<option>16</option>
								<option>17</option>
								<option>18</option>
							</select>
						</div>
					</td>
					<th style="text-align:right">生成文件地址：</th>
					<td style="font-weight:bold;">
						<div class="input-group">
							<textarea id="warPath" name="warPath" class="form-control" rows="2" style="width:100%;color:#089426;font-weight:bold;" placeholder="点击蓝色按钮打开生成文件目录；点击红色按钮可选择删除本地文件。"></textarea>
							<div class="input-group-btn">
								<button id="openWar" type="button" class="btn btn-default mbtn" onclick="openWar('warPath')" title="打开生成文件目录">
									<b style="color:blue">打开</b>
								</button>
							</div>
							<div class="input-group-btn">
								<button id="gen_pro_btn" type="button" class="btn btn-default dropdown-toggle mbtn" data-toggle="dropdown" title="可选择删除目录文件">
									<b style="color:red">删除</b>
								</button>
								<ul id="gen_pro" class="dropdown-menu pull-right">
									<c:forEach items="${pack_addr_list }" var="pro">
										<li><a id="${pro }" href="javascript:delGenFile('${pro }');">${pro }</a></li>
									</c:forEach>
								</ul>
							</div>
						</div>
					</td>
				</tr>
				
				<tr class="trBtn">
					<td colspan="4" style='text-align: center;vertical-align: center'>
						<div class="input-group" style="width:100%">
							<div class="radio icheck-primary" style="float:left;margin-left:10px">
								<input type="radio" id="not_svn" name="_svn" onclick="modelClick(this);"/>
								<label for="not_svn" style="font-weight:bold">不校验</label>
							</div>
							<div class="radio icheck-primary" style="float:left;margin-left:20px">
								<input type="radio" checked id="local_svn" name="_svn" onclick="modelClick(this);"/>
								<label for="local_svn" style="font-weight:bold">本地SVN</label>
							</div>
							<div class="radio icheck-primary" style="float:left;margin-left:20px;margin-right:1px"">
								<input type="radio" id="remote_svn" name="_svn" onclick="modelClick(this);"/>
								<label for="remote_svn" style="font-weight:bold">多人协作:</label>
							</div>
							<button id="local_cnfm_btn" class="btn btn-info" type="submit" onclick="getProFiles()" style="float:left; margin-left:272px; font-size:16px;">确认</button>
							<button id="local_pack_btn" class="btn btn-primary" type="submit" onclick="getChecked('EuiTree')"style="float:left; margin-left:80px; font-size:16px;">打包</button>
							<div class="input-group-btn" id="remote_div" style="float:left;padding-right:33px;display:none">
								<button id="svn_addr_btn" type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" title="svn项目路径">
									<b>&#8595;</b>
								</button>
								<ul id="gen_pro" class="dropdown-menu pull-left">
									<c:forEach items="${svn_addr_list }" var="pro">
										<li><a id="${pro }" href="javascript:selectSvn('${pro }','svn_addr');">${pro }</a></li>
									</c:forEach>
								</ul>
							</div>
							<textarea id="svn_addr" name="svn_addr" type="text" class="form-control svnmodel" rows="1" style="float:left;width:60%;display:none;font-weight:bold;" 
								onkeyup="getProDirs('svn_addr')" placeholder="输入 SVN 项目代码目录地址"></textarea>
							<div class="input-group svnmodel" style="float:left;">
								<div class="input-group-btn" >
									<button type="button" class="btn btn-default" title="上层目录" onclick="deleteUri('svn_addr')">&#8593;</button>
								</div>
								<div class="input-group-btn">
									<button id="add_svn_addr" type="button" class="btn btn-default dropdown-toggle" title="下层目录"
										data-toggle="dropdown" >&#8595;
									</button>
									<ul id="svn_addr_pro" class="dropdown-menu pull-right"></ul>
								</div>
							</div>
							<div class="input-group-btn svnmodel" id="remote_account" style="float:left;padding-left:20px">
								<button id="account" type="button" class="btn btn-default" title="设置svn账号">账号</button>
							</div>
						</div>
					</td>
				</tr>
				<tr class="localmodel font_diy" style="background-color:#f8fbf8;">
					<td colspan="4">
						<a class="notice">Tips： </a>
						<a class="notice">①输入项目目录</a>→
						<a class="notice">②输入升级文件目录</a>→
						<a class="notice">③设置文件名称</a>→
						<a class="notice">④点击确认</a>→
						<a class="notice">⑤核对勾选</a>→
						<a class="notice">⑥点击打包</a>→&nbsp;&nbsp;&nbsp;
						<input type="button" class="btn-success" title="注意：升级包必须在SVN的检出目录中，才可使用本功能！" id="upload" onclick="uploadFile()" value="⑦上传升级包"><%--&nbsp;&nbsp;&nbsp;→
						<input type="button" class="btn-info" onclick="showApply()" value="⑧升级部署申请">--%>
<!-- 						&nbsp;&nbsp;&nbsp;→<a class="notice" onclick="insertApply()" style="cursor:pointer;">测试</a> -->
					</td>
				</tr>
				<tr class="svnmodel font_diy" style="background-color:#f8fbf8;">
					<td colspan="4">
						<a class="notice">Tips： </a>
						<a class="notice">①选择 Svn 路径</a>→
						<a class="notice">②设置提交时间</a>→
						<a class="notice">③查询SVN提交记录</a>→
						<a class="notice">④勾选版本</a>→
						<a class="notice">⑤点击确认（核对文件）</a>→
						<a class="notice">⑥打包</a>→&nbsp;&nbsp;&nbsp;
						<input type="button" class="btn-success" id="upload_remote" onclick="uploadFile()" title="注意：升级包必须在SVN的检出目录中，才可使用本功能！" value="⑧上传升级包"><%--&nbsp;&nbsp;&nbsp;→
						<input type="button" class="btn-info" onclick="showApply()" value="⑧升级部署申请">--%>
					</td>
				</tr>
				<tr id="diff_point"></tr>
			</tbody>
		</table>
		
		<table class="table table-bordered svnmodel" id="svn_search" style="width:68%;margin:auto;font-size:14px;margin-bottom:20px">
			<tr>
				<th colspan="9" style='text-align: center;'>
					<form class="form-inline" style="vertical-align: middle; margin:0">
					    <div class="form-group">
					        <label for="begin">提交时间：</label>
					        <div class="form-group date form_datetime" data-link-field="begin">
			                    <input size="20" type="text" value="" style="font-weight:bold">
			                    <span class="add-on"><i class="">—</i></span>
								<span class="add-on"><i class="icon-th"></i></span>
			                </div>
					        <input type="hidden" class="form-control" id="begin" name="begin">
					    </div>
					    <div class="form-group">
					     <label for="begin"></label>
					        <div class="form-group date" data-link-field="end">
			                    <input id="end" size="20" type="text" value="" disabled readonly style="font-weight:bold">
			                </div>
					    </div>
					    <button type="button" class="btn btn-success" onclick="searchSvn();" style="margin-left: 10px;font-size:16px"" >查询</button>
					    <button type="button" class="btn btn-info" onclick="confirmSvn();" style="margin-left: 10px;font-size:16px"" >确认</button>
					    <button type="button" class="btn btn-primary" onclick="getChecked('EuiTree');" style="margin-left: 10px;font-size:16px"" >打包</button>
					</form>
				</th>
			</tr>
			<tbody id="delete_list"></tbody>
			<tfoot id="change_list"></tfoot>
		</table>
		
		<!-- EasyUi tree -->
		<div id="div-a" class="div-a">
			<button type="button" class="btn btn-default btn-sm" onclick="treeToggle('EuiTree');" style="margin-left:83px;float:center" >Toggle</button>
			<ul id="EuiTree" class="easyui-tree" checkbox="true" style="margin-left: 155px" data-options="animate:true">
				 <li><span>.........</span></li>
			</ul>
		</div>
		<div id="div-b" class="div-b">
			<button type="button" class="btn btn-default btn-sm" onclick="treeToggle('EuiTreeTwo');" style="float:center" >Toggle</button>
			<ul id="EuiTreeTwo" class="easyui-tree" checkbox="true" style="margin-left: 75px" data-options="animate:true">
				 <li><span>.........</span></li>
			</ul>
		</div>
		
		<table class="table table-bordered svnmodel" style="width:68%;margin:auto;font-size:14px;margin-bottom:20px">
			<tr id="author_tr"  style="font-weight:bold;"></tr>
			<tr id="version_tr" style="font-weight:bold;"></tr>
		</table>
		
		<!-- 多人协作 -->
		<table id="svn_tb" class="table table-hover table-bordered table-condensed table-striped svnmodel" style="width:68%;margin:auto;font-size:14px;"></table>
		
		<div id="footer" name="footer" style="margin-bottom: 1000px"></div>
	</div>

    <script type="text/javascript">
		
// 		toastr.options.positionClass = 'toast-bottom-center';
		toastr.options = {
			positionClass: 'toast-top-center',
			showDuration: '300',
			hideDuration: '2000',
			timeOut: '3000'
		};
		
		$(function(){
			//将项目目录存到隐藏域中
			var local_addr = $("#local_addr").val();
			if (local_addr.endWith("/")) {
				local_addr = local_addr.substring(0, local_addr.length-1);
			}
			var local_uris = local_addr.split("/");
			$("#local_addr_nm").val(local_uris[local_uris.length - 1]);
			$("#author_tr").hide();
			$("#version_tr").hide();
			$(".svnmodel").hide();
			
			getDateYMD();
			setInterval(clock, 1000);
		});
		
		
		//获取当前年月日
		function getDateYMD() {
			var nowDate = new Date();
			var year = nowDate.getFullYear();
			var month = nowDate.getMonth();
			var day = nowDate.getDate();
			var date_str = year;
			if (++month < 10) {
				month = "0" + month;
			} else {
				month = month + "";
			}
			if (day < 10) {
				day = "0" + day;
			} else {
				day = day + "";
			}
			date_str +=  month + day;
			$("#time_str").val(date_str);
		}
		
		function clock(){
			$("#end").val(curDateTime(0));
		}
		
		//获取当前时间
		function curDateTime(Minutes) {
			var d = new Date();
			d.setMinutes(d.getMinutes() + Minutes);
			var year = d.getFullYear();
			var month = d.getMonth() + 1;
			var date = d.getDate();
			var day = d.getDay();
			var curDateTime = year;
			if (month > 9)
				curDateTime = curDateTime + "-" + month;
			else
				curDateTime = curDateTime + "-0" + month;
			if (date > 9)
				curDateTime = curDateTime + "-" + date;
			else
				curDateTime = curDateTime + "-0" + date;
			var hour = d.getHours();
			if (hour < 10) {
				hour = "0" + hour;
			}
			var minute = d.getMinutes();
			if (minute < 10) {
				minute = "0" + minute;
			}
			var seconds = d.getSeconds();
			if (seconds < 10) {
				seconds = "0" + seconds;
			}
			if (Minutes == 0) {
				curDateTime += " " + hour + ":" + minute + ":" + seconds;
			} else {
				curDateTime += " " + hour + ":" + minute;
			}
			return curDateTime;
		}
		
	    $(".form_datetime").datetimepicker({
	    	language: "zh-CN",
	        format: "yyyy-MM-dd hh:ii:ss",
	        autoclose: true,
	        todayBtn: true,
	        todayHighlight: 1,
	        autoclose : 1,
			defaultDate : new Date(),
	        minuteStep: 5,
	        startView : 2,
			forceParse : 0,
			showMeridian : 1
	    });
		
		//获取项目所有文件
		function getProFiles(){
			blockUI();
			var projectUrl = $("#local_addr").val();
			if (projectUrl == "" || projectUrl == null) {
				return;
			}
			projectUrl = projectUrl.replaceAll("\\\\", "/");
			if (!projectUrl.endWith("/")){
				projectUrl += "/";
			}
			$("#local_addr").val(projectUrl);
			$("#EuiTree_addr").val(projectUrl);
			var isCheckSvn = $("#local_svn").is(':checked');
			$.ajax({
                type: "post",
                dataType: "json",
                url: "<%=path%>/pack?method=getProFiles",
                data: {"proUrl":projectUrl, "isCheckSvn":isCheckSvn},
                success: function(data) {
               		$("#webRootName").val(data.webRootName+"");
                   	var json = eval("("+data.jsonData+")");
                   	$("#EuiTree").tree({
   						data:json,
   						onlyLeafCheck:false,
   						onSelect:function(node){
   　　							$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target); 
   						},
   						onLoadSuccess:function(){
   							var nodes = $("#EuiTree").tree('getChecked');
   							if(nodes != null && nodes.length > 0){
   								for(var i=0; i<nodes.length; i++){
   									expandParent(nodes[i]);
   								}
   							}
   						}
   					});
   					if(data.version != 0){
                 			toastr.info("<b>JDK版本："+data.version+"</b>");
   					}else{
   						toastr.warning("<b>无法识别JDK版本！</b>");
   					}
   					
   					if (JSON.stringify(data.diffMap) != JSON.stringify({})) {
   						$(".diff_tr").remove();
   						$(".diff_head").remove();
   						$(".diff_foot").remove();
   						$(".diff_html").remove();
   						$(".diff_tr_odd").remove();
   						var html = 
    						"<tr class='diff_head' style='background-color:#c4c5c763;font-weight:bold;'>" +
	    						"<td>序号</td>" +
	    						"<td colspan='2'>文件地址（点击可打开本地文件）</td>" +
	    						"<td>文件名（点击可查看修改内容）</td>" +
	    					"</tr>" ;
	    				var tgleHtml = 
	    					"<tr class='diff_foot' style='background-color:#c4c5c763;font-weight:bold;'>" +
		    					"<td colspan='4'>" +
									"<a href='javascript:void(0)' onclick='tDiffList()' style='color:#ec5a05'>Toggle</a>" +
								"</td>" +
	    					"</tr>" ;
	    				var count = 1;
   						for (var key in data.diffMap) {
   							
   							var fileNm = key.split("/")[key.split("/").length - 1];
   							var clas = "diff_tr";
   							if (count%2 == 0) {
   								clas = "diff_tr_odd";
   							}
   							if (data.diffMap[key] != "none" && data.diffMap[key] != "unknown") {
	   							var info = data.diffMap[key];
	   							html +=
	    							"<tr class='"+ clas +"'>" +
			    						"<td>"+ count +"</td>" +
			    						"<td colspan='2'><a class='diff_a' onclick='openFile(\""+ key +"\")'>"+ key +"</a></td>" +
			    						"<td>" +
			    							"<a class='diff_a' href='javascript:void(0)' onclick='tDiffHtml(\"html_tr_"+ count +"\",\""+ key +"\")'>"+ fileNm +"</a>" +
			    						"</td>" +
			    					"</tr>" +
    								"<tr class='diff_html' id='html_tr_"+ count +"' flag='0'>" +
	    								"<td class='diff_td' colspan='4'><b><a href='javascript:void(0)' onclick='tDiffHtml(\"html_tr_"+ count +"\", \"\")' style='float:right;margin-right: 60px;'>[x]</a></b>" +
	    								/* "<input type='hidden' id='html_input_"+ count +"' value='"+ info +"'>" + */
	    								"<span id='html_span_"+ count +"' class='diff_span' ></span><b><a href='javascript:void(0)' onclick='tDiffHtml(\"html_tr_"+ count +"\", \"\")' style='float:right;margin-right: 60px;'>[x]</a></b></td>" +
	    							"</tr>" ;
   							} else {
   								html += 
	    							"<tr class='"+ clas +"'>" +
			    						"<td>"+ count +"</td>" +
			    						"<td colspan='2'><a class='diff_a' onclick='openFile(\""+ key +"\")'>"+ key +"</a></td>" +
			    						"<td id='td_"+ key +"'>" +
			    							"<b>"+ fileNm +"</b>" +
			    						"</td>" +
			    					"</tr>" ;
   							}
   							count++;
   						}
	    				$("#diff_point").before(html);
	    				$("#diff_point").before(tgleHtml);
   					}
   					
   					$("#div-a").css("display", "block");
   					$("#EuiTree").css("display", "block");
					$.unblockUI();
				}
			});
		}
		
		function changeType (obj) {
			var type = obj.value;
			type = type == 'SIT' ? 'LIV' : 'SIT';
			$("#update_type").val(type);
		}
		
		//获取目录
		function getProDirs(id){
			var local_addr = "";
			var pack_addr = "";
			var svnDirs_addr = "";
			var name = "";
			var password = "";
			switch (id) {
			case "local_addr":
				local_addr = $("#local_addr").val();
				break;
			case "pack_addr":
				pack_addr = $("#pack_addr").val();
				break;
			case "svnDirs_addr":
				name = $("#acc_name").val();
				password = $("#acc_password").val();
				if ($.trim(name) != "" && $.trim(password) != "") {
					svnDirs_addr = $("#svnDirs_addr").val();
				} else {
					toastr.warning("未填写账号密码！");
				}
				break;
			case "svn_addr":
				name = $("#acc_name").val();
				password = $("#acc_password").val();
				if ($.trim(name) != "" && $.trim(password) != "") {
					svnDirs_addr = $("#svn_addr").val();
				} else {
					toastr.warning("未填写账号密码！");
				}
				break;
			default:
				break;
			}
			$.ajax({
                url:"<%=path%>/pack?method=getProDirs",
                type:"post",
                dataType: "json",
                data:{"local_addr":local_addr, "pack_addr":pack_addr, "svnDirs_addr":svnDirs_addr, "name":name, "password":password, "flag":1},
                success:function(data){
                	if (data.flag!=0 && id=='local_addr') {
                		$("#local_pro").empty();
                		$.each(data.local_addr_list,function(index,value){
                			$("#local_pro").append("<li><a href=\"javascript:addUri('"+value+"', 'local_addr');\">"+value+"</a></li>");
                		});
                	}
                	if (data.flag!=0 && id=='svnDirs_addr') {
                		$("#svnDirs_pro").empty();
                		$.each(data.svnDirs_addr_list,function(index,value){
                			$("#svnDirs_pro").append("<li><a href=\"javascript:addUri('"+value+"', 'svnDirs_addr');\">"+value+"</a></li>");
                		});
                	}
                	if (data.flag!=0 && id=='svn_addr') {
                		$("#svn_addr_pro").empty();
                		$.each(data.svnDirs_addr_list,function(index,value){
                			$("#svn_addr_pro").append("<li><a href=\"javascript:addUri('"+value+"', 'svn_addr');\">"+value+"</a></li>");
                		});
                	}
                	if (data.flag!=0 && id=='pack_addr') {
                		$("#pack_pro").empty();
                		$.each(data.pack_addr_list,function(index,value){
                			$("#pack_pro").append("<li><a href=\"javascript:addUri('"+value+"', 'pack_addr');\">"+value+"</a></li>");
                		});
                		$("#gen_pro").empty();
                		$.each(data.pack_addr_list,function(index,value){
                			$("#gen_pro").append("<li><a id='"+ value +"' href=\"javascript:delGenFile('"+value+"');\">"+value+"</a></li>");
                		});
                	}
                },
                error:function(e){
                    toastr.error("获取目录失败！");
                }
            });
		}
		
		//添加选中 uri
		function addUri(uri, id){
			var url = $("#"+id).val();
			url = url.replaceAll("\\\\", "/");
			$("#"+id).val(url);
			if (!url.endWith("/")){
				url += "/";
			}
			url += uri + "/";
			$("#"+id).val($.trim(url));
			getProDirs(id);
			setProNm(uri, id+'_nm');
			$("#add_"+id).click();
		}
		
		//删除末尾 uri
		function deleteUri(id){
			var url = $("#"+id).val();
			url = url.replaceAll("\\\\","/");
			$("#"+id).val(url);
			if (url.endWith("/")) {
				url = url.substring(0,url.length-1);
			}
			var uris = url.split("/");
			var newUrl = "";
			for (var i=0; i<uris.length-1; i++) {
				if (i > 0) {
					newUrl += "/" + uris[i];
				} else {
					newUrl += uris[i];
				}
			}
			if (newUrl == "") {
				newUrl = uris[0];
			}
			$("#"+id).val(newUrl + "/");
			getProDirs(id);
			var pro_nm = "";
			if (uris.length == 1) {
				pro_nm = uris[0];
			} else {
				pro_nm = uris[uris.length - 2];
			}
			setProNm(pro_nm, id + '_nm');
		}
		
		String.prototype.endWith = function(str){  
			var reg = new RegExp(str + "$");
			return reg.test(this);
		}
		String.prototype.replaceAll = function(s1, s2){
　　			return this.replace(new RegExp(s1, "gm"), s2);
　　		}
		String.prototype.startWith = function(str){
		    var reg=new RegExp("^"+str);
		    return reg.test(this);
		}
		
		function setProNm(pro_nm, id){
			$("#"+id).val(pro_nm);
		}
		
		//获取生成文件名称
		function getGenName(){
			var file_name = "";
			var webRootName = $("#webRootName").val();
			var update_type = $("#update_type").val();
			var time_str = $("#time_str").val();
			var num_str = $("#num_str option:selected").val();
			webRootName = $.trim(webRootName).toUpperCase();
			update_type = $.trim(update_type);
			time_str = $.trim(time_str);
			num_str = $.trim(num_str);
			file_name = webRootName+"-"+update_type +"-"+time_str+"-"+num_str;
			return file_name;
		}
		
		//打包 
		function getChecked(id){
			blockUI();
			var file_name = getGenName();
			if ($('#'+id).data != null && $('#'+id).data != undefined) {
				var nodes = $('#'+id).tree('getChecked');
				if (nodes != null && nodes.length > 0) {
					var checkedUrl = '';
					for (var i=0; i<nodes.length; i++) {
						if (checkedUrl != '') {
							checkedUrl += ',';
						}
						checkedUrl += nodes[i].attributes.url;
					}
				} else {
					toastr.warning("请先勾选文件！");
					unblockUI();
					return;
				}
			} else {
				toastr.warning("请先勾选文件！");
				unblockUI();
				return;
			}
			if (checkedUrl == "" && id == "EuiTree") {
				toastr.warning("请先勾选文件！");
				unblockUI();
				return;
			}
			var pack_addr = $("#pack_addr").val();
			if (!pack_addr.endWith("/")) {
				pack_addr += "/";
				pack_addr = pack_addr.replaceAll("\\\\", "/");
				pack_addr = pack_addr.replaceAll("//", "/");
			}
			//校验文件名是否存在
			var isFileExist = false;
			$.ajax({
                url: "<%=path%>/pack?method=isFileExist",
                type: "post",
                async: false,
                dataType: "json",
                data: {"pack_addr":pack_addr, "file_name":file_name},
                success: function(data){
                	if(data.flag == true){
	                	bootbox.alert({ 
					    	title: "<div style=\"font-size:16px;font-weight:800;\">提示：</div>",
					    	message: "<div class=\"text-center\" style=\"font-size:15px;\"><b>文件已存在，【更改文件名序号】  或  点击红色链接【删除本地文件】后重试！</b></div>",
							callback: function () {
								$("#gen_pro_btn").click();
			                	$("#" + file_name).css("color", "red");
							}
					    });
	                	isFileExist = true;
	                	//$("#warPath").val(file_name);
					}
                },
                error:function(e){
                	isFileExist = true;
                    toastr.error("验证文件名是否存在失败！");
                }
            });
			if (isFileExist) {
				unblockUI();
				return false;
			} else {
				$("#pack_addr").val(pack_addr);
				var local_addr_nm = $("#local_addr_nm").val();
				var projectUrl = $("#local_addr").val();
				var webRootName = $("#webRootName").val();
				$.ajax({
	                url: "<%=path%>/pack?method=packWar",
	                type: "post",
	                dataType: "json",
	                data: {"checkedUrl":checkedUrl, "pack_addr":pack_addr,
	                	"projectUrl":projectUrl, "webRootName":webRootName,
	                	"projectName":local_addr_nm, "file_name":file_name},
	                success: function(data){
	                	$("#warPath").val(data.warPath);
	                	$("#gen_nm").val(data.fileStr);
	                	getProDirs('pack_addr');
	                	if(typeof(data.msg)!="undefined"){
		                	bootbox.alert({
									  title: "<div style=\"font-size:16px;font-weight:800;\">无法打包以下文件：</div>",
									  message: "<div class=\"text-center\"><b>"+data.msg+"</b></div>"
						    });
						}
	                	if(id=="EuiTree"){
	                		getWarInfo("EuiTreeTwo");
	                	}
	                	if(id=="EuiTreeTwo"){
	                		getWarInfo("EuiTree");
	                	}
	                	$("#div-b").css("display", "block");
	                	$("#EuiTreeTwo").css("display", "block");
	                },
	                error: function(e){
	                    toastr.error("打包失败！");
	                }
	            });
				unblockUI();
			}
		}
		
		//待删除
		function compareFile(){
			var local_addr_nm = $("#local_addr_nm").val();
			var localProUrl = $("#local_addr").val();
			localProUrl = localProUrl.replaceAll("\\\\", "/");
			if (!localProUrl.endWith("/")){
				localProUrl += "/";
			}
			$("#local_addr").val(localProUrl);
			$("#EuiTree_addr").val(localProUrl);
			var svn_addr_nm = $("#svn_addr_nm").val();
			var svn_addr = $("#svn_addr").val();
			svn_addr = svn_addr.replaceAll("\\\\", "/");
			if (!svn_addr.endWith("/")){
				svn_addr += "/";
			}
			$("#svn_addr").val(svn_addr);
			$("#EuiTreeTwo_addr").val(svn_addr);
			$.ajax({
                type: "post",
                dataType: "json",
                url: "<%=path%>/pack?method=doCompareFile",
                data: {"localProUrl":localProUrl, "svnProUrl":svn_addr},
                success: function(data) {
                	var json = eval("("+data.compareData+")");
                	getProFiles();
                	$("#EuiTreeTwo").tree({
						data:json,
						onlyLeafCheck:false,
						onSelect:function(node){
　　							$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target); 
						},
						onLoadSuccess:function(){
						}
					});
					toastr.info('校验成功！');
					return;
				}
			});
		}
		
		function getWarInfo(id){
			var warPath = $("#warPath").val();
			if(warPath == ""){
				toastr.error("获取 War 包地址失败！");
				return;
			}
			$.ajax({
                type: "post",
                dataType: "json",
                url: "<%=path%>/pack?method=getWarInfo",
                data: {"warPath":warPath},
                success: function(data) {
                	var json = eval("("+data.warData+")");
                	$("#"+id).tree({
						data:json,
						onlyLeafCheck:true,
						onSelect:function(node){
							bootbox.confirm({ 
							  size: "small",
							  title: "<div style='font-size:16px;font-weight:bold;'>确认删除</div>",
							  message: "<div class='text-center'><b>"+node.text+"</b></div>",
							  buttons: {
					              confirm: {
					                  label: '确认',
					                  className: 'btn-success'
					              },
					              cancel: {
					                  label: '取消',
					                  className: 'btn-default'
					              }
				              },
							  callback: function(result){
							    if(result && node.attributes.url == "") {
							    	return;
							    }
							  	if(result){
					            	$.post('<%=path%>/pack?method=deleteUrl',
					                	{"deleteUrl":node.attributes.url,"warPath":warPath},
						                function(data){
						                    if(data.flag == 1){
							  					toastr.success('删除成功！');
						                    }
					                    	var json = eval("("+data.warReload+")");
					                    	$("#"+id).tree({
												data:json
											});
					                    	$("#"+id).tree("expandAll");
					                	}
					                ); 
					            }
							  }
							});
						},
// 						onDblClick: function(node){
// 							$(this).tree(node.checked === 'false' ? 'check' : 'uncheck', node.target);
// 							node.checked = node.checked === 'false'? 'true' : 'false';
// 						},
					});
                	$("#"+id).tree("expandAll");
					toastr.success('打包成功！');
				}
			});
		}
		
		//复制文件【未使用】
		function copyFile(id) {
			var nodes = $('#'+id).tree('getChecked');
			var checkedUrl = '';
			for(var i=0; i<nodes.length; i++){
				if (checkedUrl != '') checkedUrl += ',';
				checkedUrl += nodes[i].attributes.url;
			}
			if (checkedUrl == "") {
				toastr.warning("请勾选左侧文件，再复制！");
				return;
			}
			var pack_addr= $("#pack_addr").val();
			if (!pack_addr.endWith("/")){
				pack_addr += "/";
			}
			$("#pack_addr").val(pack_addr);
			var projectUrl = $("#EuiTree_addr").val();
			$.ajax({
                url:"<%=path%>/pack?method=copyCheckedFile",
                type:"post",
                dataType: "json",
                data:{"checkedUrl":checkedUrl, "pack_addr":pack_addr, "projectUrl":projectUrl},
                success:function(data){
                	$("#warPath").val(data.warPath);
                },
                error:function(e){
                    toastr.error("复制失败");
                }
            });
		}
		
		function openFile(path) {
			$.post('<%=path%>/pack?method=openDir',
       			{"path":path},
       			function(data){
       				if(data.result == 1){
       					toastr.error("地址不存在！");
       				}
       			}
       		);
		}
		
		function openWar(id){
			var path = $.trim($("#" + id).val());
			$.post('<%=path%>/pack?method=openDir',
      			{"path":path},
      			function(data){
      				if(data.result == 1){
      					toastr.error("地址不存在！");
      				}
      			}
      		);
		}
		
		//删除文件，id为目录名
		function delGenFile(id){
			id = $.trim(id);
			var warPath = "";
			var gen_nm = id;
			if(id != ''){
				var pack_addr = $("#pack_addr").val();
				pack_addr = pack_addr.replaceAll("\\\\", "/");
				if(!pack_addr.endWith("/")){
					pack_addr += "/";
				}
				warPath = pack_addr + gen_nm;
			} else {
				//未使用
				warPath = $("#warPath").val();
				if (warPath == "") {
					toastr.warning("文件地址为空！");
					return;
				}
			}
			bootbox.confirm({ 
				size: "big",
				title: "<div style='font-size:16px;font-weight:bold;'>确认删除</div>",
				message: "<div class='text-center'><b>"+warPath+"</b></div>",
				buttons: {
					confirm: {
						label: '确认',
						className: 'btn-success'
					},
					cancel: {
						label: '取消',
						className: 'btn-default'
					}
				},
				callback: function(result){
					if(result){
			        	$.post('<%=path%>/pack?method=deleteUrl',
			              	{deleteUrl:warPath, warPath:warPath, f:1},
							function(data){
								if(data.flag == 1){
									toastr.success('删除成功！');
									if(id == ''){
										$("#warPath").val("");
										$("#gen_nm").val("");
										getProDirs('pack_addr');
									} else {
										getProDirs('pack_addr');
										$("#gen_pro_btn").click();
									}
									var rootNode = $("#EuiTreeTwo").tree("getRoot");
									if(rootNode.text == id){
										var json = eval("("+data.warReload+")");
				                    	$("#EuiTreeTwo").tree({
											data:json
										});
				                    	$("#EuiTreeTwo").tree("expandAll");
									}
								} else {
									toastr.error('删除失败！');
								}
							}
						);
					}
				}
			});
		}
		
		function modifyName(type){
			var file_addr = $("#warPath").val();
			file_addr = $.trim(file_addr);
			if (file_addr == "") {
				toastr.warning("文件地址不存在！");
				return;
			}
			var new_file_nm = "";
			if (type == 'modify'){
				var webRootName = $("#webRootName").val();
				var update_type = $("#update_type").val();
				var time_str = $("#time_str").val();
				var num_str = $("#num_str option:selected").val();
				webRootName = $.trim(webRootName).toUpperCase();
				update_type = $.trim(update_type);
				time_str = $.trim(time_str);
				num_str = $.trim(num_str);
				new_file_nm = webRootName +"-"+update_type +"-"+time_str+"-"+num_str;
			} else if (type == 'update') {
				new_file_nm = $("#gen_nm").val();
				new_file_nm = $.trim(new_file_nm);
			}
			var url = '<%=path%>/pack?method=modifyName';
			$.ajax({
				type : "post",
				dataType : "json",
				url : url,
				data : {"file_addr":file_addr, "new_file_nm":new_file_nm},
				success : function(data){
					$("#warPath").val(data.file_addr);
					$("#gen_nm").val(new_file_nm);
					getProDirs('pack_addr');
					toastr.success("修改文件名成功！")
				},
				error : function(e){
					toastr.error("修改失败,文件名已存在！");
				}
			});
		}
		
		//展开父节点
		function expandParent(node){
	    	var parentNode = $("#EuiTree").tree("getParent", node.target);
		    if(parentNode != null && parentNode != "undefined"){
		    	$("#EuiTree").tree("expand", parentNode.target);
		        expandParent(parentNode);
		    }  
		};
		
		function blockUI(){
			var html = 
				"<table width='100%' style='cursor: default'>" +
					"<tr>" +
						"<td id='a_td' align='right' style='padding-top: 5px;padding-right: 9px;display:none'>" +
							"<a onclick='$.unblockUI();'>[x]</a>" +
						"</td>" +
					"</tr>" +
					"<tr>" +
						"<td id='msg_td' style='font-size:16px;font-weight:800; text-align:center; padding:16px;color:#337ab7'>" +
							"<img src='<%=path%>/scripts/images/loading-1.gif' style='width:22px;align:absmiddle'/>" +
							" 请等待......" +
						"</td>" +
					"</tr>" +
				"</table>";
			$.blockUI({ 
				message: html,
				overlayCSS: { 
					backgroundColor:'#000',
					opacity:0.2,
				},
				css: {
					padding:	0,
					margin:		0,
					width:		'40%',
					top:		'34%',
					left:		'30%',
					textAlign:	'center',
					color:		'#000',
					border:		'3px solid #aaa',
					backgroundColor:'#fff',
				},
				fadeIn:  200,
				fadeOut:  400,
			});
			setTimeout("$('#a_td').css('display', 'block');$('#msg_td').css('padding-top', '');",60000);
		}
		
		function unblockUI() {
			$.unblockUI();
		}
		
		//查询svn日志
		function searchSvn() {
			var account_flag = $("#account_flag").val();
			if(account_flag != "true") {
				validateSvn('', '');
				return;
			}
			var acc_name = $("#acc_name").val();
			var acc_password = $("#acc_password").val();
			resetTree();
			var rootUrl = $("#svn_addr").val();
			if (rootUrl == "" || rootUrl == null) {
				toastr.warning("请输入svn目录！");
				return;
			}
			rootUrl = rootUrl.replaceAll("\\\\", "/");
			if (rootUrl.endWith("/")){
				rootUrl = rootUrl.substring(0, rootUrl.length -1);
			}
			
			//开始时间
			var begin = $.trim($("#begin").val());
			if (begin == "") {
				toastr.warning("请输入开始时间！");
				return;
			}
			if (compareDate(begin, curDateTime(0))) {
				toastr.warning("提交时间必须小于当前时间");
				return;
			}
			$("#delete_list").empty();
			$("#change_list").empty();
			$("#svn_tb").empty();
			$("#author_tr").empty();
			$("#version_tr").empty();
			
			$("#EuiTree").css("display", "none");
			$("#EuiTreeTwo").css("display", "none");
			
			$.ajax({
				type : "post",
				dataType : "json",
				url : "<%=path%>/pack?method=getSvnInfos",
				data : {
					"name" : acc_name,
					"password" : acc_password,
					"rootUrl" : rootUrl,
					"begin" : begin,
					"end" : curDateTime(0)
				},
				success : function(data){
					var logInfos = data.logInfos
					if(logInfos != null && logInfos.length > 0){
						var html = 
							"<tr style='font-weight: 800;background-color: #F5F5F5'>" +
								"<td>" +
									"选择" +
									"<input type='hidden' id='targetAuthor' value='all'>" +
								"</td>" +
								"<td width='10%'>作者</td>" +
								"<td width='14%'>版本</td>" +
								"<td width='12%'>提交操作</td>" +
								"<td width='20%'>日期</td>" +
								"<td width='26%'>信息</td>" +
								"<td width='12%'>操作</td>" +
							"</tr>" ;
						for(var i = logInfos.length - 1, j = 1; i >= 0; i--){
							var options = logInfos[i].options;
							var option = "";
							for(var n=0; n < options.length; n++){
								if (options[n] == "删除") {
									option += "<b style='float:right;color:red'>"+ options[n] +"</b>";
								}
								if (options[n] == "新增") {
									option += "<b style='float:left;color:blue'>"+ options[n] +"</b>";
								}
								if (options[n] == "修改") {
									option += "<b style='float:center;color:green'>"+ options[n] +"</b>";
								}
							}
							var content = "";
							if(logInfos[i].changedUrls.length > 0){
								for(var m = 0; m < logInfos[i].changedUrls.length; m++){
									content+="<div style='font-size:12px;font-weight:bold;padding:5px;cursor:pointer;background-color:#FFE7BA'>"+logInfos[i].changedUrls[m].url+"</div>";
								}
							}
							
							html += 
								"<tr class='svn_tr' name='"+ logInfos[i].author +"'>" +
									"<td>" +
										"<div class='checkbox icheck-primary cb' style='float:right;'>" +
											"<input type='radio' id='"+ logInfos[i].version +"' name='version_radio' onclick='radioClick(this);'/>" +
											"<label for='"+ logInfos[i].version +"' style='font-weight:700'></label>" +
										"</div>" +
										"<div class='showdiv' id='show_"+ logInfos[i].version +"' style='display:none;text-align:left; position:absolute;z-index:1000;background:#fff; width:772px;border:1px solid #ccc;margin-top:40px'>" +
										"" + content + "</div>" +
									"</td>" +
									"<td>"+ logInfos[i].author +"</td>" +
									"<td style='font-weight:600'>"+ logInfos[i].version +"</td>" +
									"<td>"+ option +"</td>" +
									"<td style='font-weight:600'>"+ logInfos[i].date +"</td>" +
									"<td>"+ logInfos[i].info +"</td>" +
									"<td>" +
										"<button class='btn btn-info btn-sm show-div' onclick='getDetails(\""+ logInfos[i].version +"\")' >详情</button>" +
									"</td>" +
								"</tr>" ;
							j++;
						}
						$("#svn_tb").append(html);
						//设置 作者、版本 行
						$("#author_tr").append("<th width='80px'><input type='hidden' name='size' id='size' value=''><input type='button' id='bt0' class='btn-sm btn-success' value='全部' onclick='selectAuthor(\"all\");'/></th>");
						$("#version_tr").append("<td width='80px'><span id='span0' name='span_all' onclick='clearSpan(this);'></span></td>");
						var i = 1;
						for(var key in data.authorMap){
							$("#author_ul").append("<li><a href=\"javascript:selectAuthor('"+ data.authorMap[key] +"');\">"+ data.authorMap[key] +"</a></li>");
							$("#author_tr").append("<th width='80px'><input type='hidden' name='size' id='size' value=''><input type='button' class='btn-sm btn-success' id='bt"+ i +"' value='"+ data.authorMap[key] +"' onclick='selectAuthor(\""+ data.authorMap[key] +"\");'/></th>");
							$("#version_tr").append("<td width='80px'><span id='span"+ i +"' name='span_"+ data.authorMap[key] +"' onclick='clearSpan(this);'></span></td>");
							i++;
						}
						var size = Object.keys(data.authorMap).length;
						$("#size").val(size);
						var colspan = 20 - size;
						$("#author_tr").append("<td class='notice' colspan='"+ colspan +"'><a> ← 多人协作时，可点击提交者名称，勾选对应版本号。</a></td>");
						$("#version_tr").append("<td class='notice' colspan='"+ colspan +"'><a> ← 点击版本号，取消勾选。</a></td>");
						
						$("#author_tr").show();
						$("#version_tr").show();
					}
				},
				error : function(e){
					toastr.error("获取svn日志失败！");
				}
			});
		}
		
		function radioClick(obj) {
			var name = $("#targetAuthor").val();
			$("span[name=span_" + name +"]").html(obj.id);
			//行标记颜色
// 			$(obj).parent().parent().parent().css("background-color", "rgb(255, 250, 205, 0.6)");
			if (name != "all") {
				$("#span0").html("");
			} else {
				var size = parseInt($("#size").val());
				for (var i = 1; i <= size; i++) {
					$("#span" + i).html("");
				}
			}
			
		}
		
		function clearSpan(obj) {
			$(obj).html("");
			$(".cb").each(function(){
				$(this).iCheck('uncheck');
			});
		}
		
		//切换模式
		function modelClick(obj) {
			if (obj.id == "remote_svn") {
				$("#local_cnfm_btn").hide();
				$("#local_pack_btn").hide();
				$(".localmodel").hide();
				$(".svnmodel").show();
				$("#remote_div").show();
				$("#svn_addr_btn").show();
				$("#svn_addr").show();
				validateSvn('', '');
			} else {
				$(".localmodel").show();
				$("#local_cnfm_btn").show();
				$("#local_pack_btn").show();
				$(".svnmodel").hide(); 
				$("#remote_div").hide();
				$("#svn_addr_btn").hide();
				$("#svn_addr").hide();
			}
			resetTree();
		}
		
		function validateSvn(name, password) {
			var _flag = $("#account_flag").val();
			if (_flag) {
				return true;
			}
			var html = 
				"<table>" +
					"<tr>" +
						"<th width='40%'>账号：</th>" +
						"<td width='60%'><input type='text' name='name' id='name' value='"+name+"' /></td>" +
					"</tr>" +
					"<tr>" +
						"<th>密码：</th>" +
						"<td><input type='text' name='password' id='password' value='"+password+"' /></td>" +
					"</tr>" +
				"</table>" ;
			bootbox.confirm({
				size: "small",
				title: "<div style='font-size:16px;font-weight:bold;'>SVN账户</div>",
				message: html,
				buttons: {
					confirm: {
					    label: '确认',
					    className: 'btn-success'
					},
					cancel: {
					    label: '取消',
					    className: 'btn-default'
					}
				},
				callback: function(result){
					var _name = $.trim($("#name").val());
					var _password = $.trim($("#password").val());
					var _svnUrl = $.trim($("#svn_addr").val());
					if (result) {
						if (_name != "" && _password != "") {
							$("#acc_name").val(_name);
							$("#acc_password").val(_password);
							$.ajax({
								url: "<%=path%>/pack?method=validateSvn",
								type: "POST",
								data: {
									"name" : _name,
									"password" : _password,
									"svnUrl" : _svnUrl
								},
								dataType: "json",
								success: function(data) {
									$("#account_flag").val("true");
									toastr.success("账号设置成功！");
								},
								error:function() {
									toastr.error("Svn账号设置失败！");
								}
							});
						} else {
							validateSvn(_name, _password);
						}
					}
					return true;
				}
			});
		}
		
		$("#account").click(function(){
			var html = 
				"<table>" +
					"<tr>" +
						"<th width='40%'>账号：</th>" +
						"<td width='60%'><input type='text' name='name' id='name' value='' /></td>" +
					"</tr>" +
					"<tr>" +
						"<th>密码：</th>" +
						"<td><input type='text' name='password' id='password' value='' /></td>" +
					"</tr>" +
				"</table>" ;
			bootbox.confirm({
				size: "small",
				title: "<div style='font-size:16px;font-weight:bold;'>SVN账户</div>",
				message: html,
				buttons: {
					confirm: {
					    label: '确认',
					    className: 'btn-success'
					},
					cancel: {
					    label: '取消',
					    className: 'btn-default'
					}
				},
				callback: function(result){
					if (result) {
						var _name = $.trim($("#name").val());
						var _password = $.trim($("#password").val());
						var _svnUrl = $.trim($("#svn_addr").val());
						if (_name != "" && _password != "") {
							$("#acc_name").val(_name);
							$("#acc_password").val(_password);
							$.ajax({
								url: "<%=path%>/pack?method=validateSvn",
								type: "POST",
								data: {
									"name" : _name,
									"password" : _password,
									"svnUrl" : _svnUrl
								},
								dataType: "json",
								success: function(data) {
									$("#account_flag").val("true");
									toastr.success("svn设置成功！");
									if (data.result != 1) {
										toastr.warning("无法连接svn地址！");
									}
								},
								error:function() {
									toastr.error("svn设置失败！");
								}
							});
						} else {
							toastr.error("svn设置失败！");
						}
					}
				}
			});
		});
		
		function selectSvn(url,id) {
			var realUrl = $.trim(url.split("_:_")[1]);
			if (realUrl != "") {
				url = realUrl;
			}
			$("#" + id).val(url);
			if(id == "svnDirs_addr"){
				getProDirs('svnDirs_addr');
			}
			if(id == "svn_addr"){
				getProDirs('svn_addr');
			}
		}
		
		function selectAuthor(name) {
			$("#targetAuthor").val(name);
			$(".cb").each(function(){
				$(this).iCheck('uncheck');
			});
			$(".svn_tr").each(function(){
				$(this).hide();
			});
			if (name != "all") {
				$("#span_0").html("");
				$("tr[name="+ name +"]").each(function(){
					$(this).show();
				});
			} else {
				$(".svn_tr").each(function(){
					$(this).show();
				});
			}
			var _id = parseInt($("span[name=span_"+ name +"]").html());
			if (_id != "") {
				$("#" + _id).click();
			}
		}
		
		//确认svn文件信息
		function confirmSvn() {
			var account_flag = $("#account_flag").val();
			if(account_flag != "true") {
				validateSvn('', '');
				return;
			}
			var acc_name = $("#acc_name").val();
			var acc_password = $("#acc_password").val();
			
			$("#delete_list").empty();
			$("#change_list").empty();
			$("#end").val(curDateTime(0));
			var projectUrl = $("#local_addr").val();
			if (projectUrl == "" || projectUrl == null) {
				toastr.warning("请选择项目目录！");
				return;
			}
			projectUrl = projectUrl.replaceAll("\\\\", "/");
			if (!projectUrl.endWith("/")){
				projectUrl += "/";
			}
			var rootUrl = $("#svn_addr").val();
			if (rootUrl == "" || rootUrl == null) {
				toastr.warning("请输入svn目录！");
				return;
			}
			rootUrl = rootUrl.replaceAll("\\\\", "/");
			if (rootUrl.endWith("/")){
				rootUrl = rootUrl.substring(0, rootUrl.length -1);
			}
			
			var size = $("#size").val();
			var tmp_val = "";
			for (var i = 0; i <= size; i++) {
				if ($("#span" + i).length > 0) {
					tmp_val += $.trim($("#span" + i).html());
					
				}
			}
			if (tmp_val == "") {
				toastr.warning("请勾选svn版本号！");
				return;
			}
			var authors = "";
			var versions = "";
			var size = parseInt($("#size").val());
			for (var i = 0; i <= size; i++) {
				var _author = $("#bt" + i).val();
				authors += _author + ",";
				var _version = $("#span" + i).html();
				versions += _version + ",";
			}
			authors = authors.substring(0, authors.length - 1);
			versions = versions.substring(0, versions.length - 1);
			$.ajax({
				type : "post",
				dataType : "json",
				url : "<%=path%>/pack?method=getSvnInfos",
				data : {
					"name" : acc_name,
					"password" : acc_password,
					"rootUrl" : rootUrl,
					"proUrl" : projectUrl,
					"authors" : authors, 
					"versions" : versions, 
					"flag" : 1
				},
				success : function(data){
                	$("#webRootName").val(data.webRootName+"");
                	var json = eval("("+data.jsonData+")");
                	$("#EuiTree").tree({
						data:json,
						onlyLeafCheck:false,
						onSelect:function(node){
　　							$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target); 
						},
						onLoadSuccess:function(){
							var nodes = $("#EuiTree").tree('getChecked');
							if(nodes != null && nodes.length > 0){
								for(var i=0; i<nodes.length; i++){
									expandParent(nodes[i]);
								}
							}
						}
					});
                	//删除列表delete_list
					if (JSON.stringify(data.dMap) != JSON.stringify({})) {
						var _tr1 = "";
						var _tr2 = "";
						for(var key in data.dMap){
							if (data.dMap[key] == "A") {
								_tr1 = "<li>"+ key +"</li>";
							}
							if (data.dMap[key] == "D") {
								_tr2 = "<li>"+ key +"</li>";
							}
						}
						if (_tr1 != "") {
							_tr1 = "<tr class='warn_tr1'><td>删除后又新增</td><td><ul>"+_tr1+"</ul></td></tr>"
							$("#delete_list").append(_tr1);
						}
						if (_tr2 != "") {
							_tr2 = "<tr class='warn_tr2'><td>删除</td><td><ul>"+_tr2+"</ul></td></tr>"
							$("#delete_list").append(_tr2);
						}
					}
					//新增、修改列表change_list
					if (JSON.stringify(data.cMap) != JSON.stringify({})) {
						var _tr = "";
						for(var key in data.cMap){
							if (data.cMap[key] == "N") {
								_tr += "<li style='color:red'>"+ key +"[本地不存在]</li>";
							} else {
								_tr += "<li>"+ key +"</li>";
							}
						}
						if (_tr != "") {
// 							_tr = "<tr class='warn_tr3'><th colspan='2'><a onclick='clistToggle()'>Toggle</a></th></tr>" +
							_tr = "<tr class='warn_tr3'><td>打包列表</td><td><ul>"+_tr+"</ul></td></tr>";
							$("#change_list").append(_tr);
						}
					}
					if(data.version != 0){
              			toastr.info("<b>JDK版本："+data.version+"</b>");
					}else{
						toastr.warning("<b>无法识别JDK版本！</b>");
					}
					$("#div-a").css("display", "block");
					$("#EuiTree").css("display", "block");
				},
				error : function(e){
					toastr.error("获取svn确认信息失败！");
				}
			});
		}
		
		function getDetails(no) {
			$(".showdiv").hide();
		    var event = window.event || arguments.callee.caller.arguments[0];
		    event.stopPropagation();//阻止事件冒泡
		    $("#show_" + no).show();
		    var tag = $("#show_" + no);
		    var flag = true;
		    $(document).bind("click",function(e){
		        var target = $(e.target);
		        if(target.closest(tag).length == 0 && flag == true){
		            $(tag).hide();
		            flag = false;
		        }
		    });
		}
		
		function treeToggle(id) {
			$("#"+id).toggle();
		}
		
		function resetTree() {
			$("#EuiTree").css("display", "none");
			$("#EuiTreeTwo").css("display", "none");
			var nodes = $("#EuiTree").tree('getChecked');
			if(nodes != null && nodes.length > 0){
				var root = $("#EuiTree").tree('getRoot');
				$("#EuiTree").tree('uncheck', root.target);
			}
		}
		
		function compareDate(s1,s2){
			return ((new Date(s1.replace(/-/g,"\/")))>(new Date(s2.replace(/-/g,"\/"))));
		}
		
		function uploadFile(){
			var result = validateSvn("", "");
			if (result) {
				var name = $("#acc_name").val();
				var password = $("#acc_password").val();
				var warPath = $("#warPath").val();
				if ($.trim(name) == "" || $.trim(password) == "") {
					toastr.error("账号密码为空!");
					return;
				}
				bootbox.confirm({ 
				  size: "big",
				  title: "<div style='font-size:16px;font-weight:bold;'>确认</div>",
				  message: "<div class='text-center' style='word-wrap:break-word;font-size:14px;'><b>"+"请确认升级包目录是检出目录:</b></div><div class='text-center'><b>【升级包上传目录："+ $("#pack_addr").val() +"】"+"</b></div>",
				  buttons: {
		              confirm: {
		                  label: '上传',
		                  className: 'btn-success'
		              },
		              cancel: {
		                  label: '取消',
		                  className: 'btn-default'
		              }
	              },
				  callback: function(result){
				  	  if(result){
		            	  $.post('<%=path%>/pack?method=uploadFile',
		                	  {"warPath":warPath,"name":name,"password":password},
			                  function(data){
			                      if(data.result == 1){
				  					  toastr.success('上传成功！');
			                      }
			                      if(data.result == 0){
				  					  toastr.error('升级包不存在！');
			                      }
			                      if(data.result == -1){
				  					  toastr.error('加入失败，请检查检出目录，手动上传！');
			                      }
			                      if(data.result == -2){
				  					  toastr.error('提交失败，请检查检出目录，手动上传！');
			                      }
		                	  }
		                  ); 
		              }
				   }
				});
			}
		}
		
		function showApply() {
			$(".svnDirs").show();
		}
		
		//插入数据库，增加确认页面
		function insertApply() {
			var svnUrl = $.trim($("#svnDirs_addr").val());
			svnUrl = svnUrl.replaceAll("\\\\", "/");
			if (svnUrl.endWith("/")) {
				svnUrl = svnUrl.substring(0, svnUrl.length - 1);
			}
			var svnUpperUrl = svnUrl.substring(0, svnUrl.lastIndexOf("/"));
			var apply = "";
			var apply_no = getGenName();
			$.post('<%=path%>/pack?method=insertApply',{"svnUpperUrl":svnUpperUrl, "svnUrl":svnUrl, "apply_no":apply_no},
                function(data){
                    if(data.result == 1){
	  					toastr.success('发布成功！自动跳转发布申请页面');
                    }
                    if(data.result == 0){
                    	toastr.eror('发布失败？？？');
                    }
               	}
            ); 
		}
		
		function tDiffHtml(id, path) {
			var no = id.replace("html_tr_", "");
			var flag = $("#" + id).attr('flag');
			var isGet = $("#" + id).attr('isGet');
			if (flag == 0) {
				if (isGet != 1) {
					$.ajax({
						type : "post",
						dataType : "json",
						url : "<%=path%>/pack?method=getDiff",
						data : {
							"path" : path
						},
						success : function(data){
							if (data.status == 1) {
								$("#html_span_" + no).text(data.diff);
								$("#" + id).attr('isGet', 1);
							}
							if (data.status == 0) {
								toastr.error("获取修改信息失败！");
							}
						},
						error : function(e){
							toastr.error("获取修改信息失败！");
						}
					});
				}
				$("#" + id).attr('flag', 1);
				$("#" + id).show();
			} else {
				$("#" + id).hide();
				$("#" + id).attr('flag', 0);
			}
		}
		
		function tDiffList() {
			$(".diff_tr").toggle();
			$(".diff_td").toggle();
			$(".diff_tr_odd").toggle();
			$(".diff_html").toggle();
			$(".diff_head").toggle();
		}
		
		$("#submitBtn").click(function(){
			window.open("http://10.10.18.10:8080"); 
		});
		
		$("#confirm_svnurl").click(function(){
			var war_url = $("#svnDirs_addr").val();
			war_url = war_url.replaceAll("\\\\", "/");
			if (war_url.endWith("/")) {
				war_url = war_url.substring(0, war_url.length - 1);
			}
			
			var fbsqh = war_url.split("/")[war_url.split("/").length - 1];
			$("#fbsqh").val(fbsqh);// 发布申请号
			
			if (fbsqh.split("_")[1] == "SIT" || fbsqh.split("-")[1] == "SIT") {
				$("#fwqip").val("测试");
			}
			if (fbsqh.split("_")[1] == "LIV" || fbsqh.split("-")[1] == "LIV") {
				$("#fwqip").val("生产");
			}
			
			war_url = war_url.substring(0, war_url.lastIndexOf("/"));
			$("#fbwdlj").text(war_url);// 发布文档地址
			
			$("#_xwkssj").val(curDateTime(1));
			$("#xwkssj").val(curDateTime(1));
			$("#_xwjssj").val(curDateTime(31));
			$("#xwjssj").val(curDateTime(31));
			
		});
		
	</script>
</body>

</html>
