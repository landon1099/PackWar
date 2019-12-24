<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<% String path = request.getContextPath(); %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>SVN-Statistics</title>
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

    <script type="text/javascript" src="<%=path%>/scripts/js/svg/svg.js"></script>
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
<div id="spot" class="div-center font_c" width="100%">

</div>

<script type="text/javascript">
    $(function () {
        var data = eval('(' + '${data}' + ')');
        var map = data.map;
        var start_html = "<svg width='1000' height='132'>" +
            "<g transform='translate(30, 20)'>";
        var end_html =
            "<text class='day' dx='-30' dy='' style='display: none;'>Sun</text>" +
            "<text class='day' dx='-30' dy='26'>Mon</text>" +
            "<text class='day' dx='-30' dy='' style='display: none;'>Tue</text>" +
            "<text class='day' dx='-30' dy='58'>Wed</text>" +
            "<text class='day' dx='-30' dy='' style='display: none;'>Thu</text>" +
            "<text class='day' dx='-30' dy='90'>Fri</text>" +
            "<text class='day' dx='-30' dy='' style='display: none;'>Sat</text>" +
            "</g>" +
            "</svg>";
        var x_offset = 0;
        var y_offset = 0;

        var month = 1;
        var day = 1;
        var isSetMonth = true;
        var _daysInMonth = daysInMonth(2019, 1);
        while (true) {
            if (isSetMonth) {
                start_html += "<text class='month' x='" + x_offset + "' y='-9'>" + month + "</text>";
                isSetMonth = false;
            }
            //1月开始第一周
            if (month == 1 && day == 1) {
                var dayInweek = getWeek(2019, 1, 1);
                start_html += "<g transform='translate(" + x_offset + ", 0)'>";
                var tmp_html = "";
                for (var n = dayInweek; n < 7; n++) {
                    y_offset = n * 16;
                    var mm_dd = getCurrent_MM_dd(month, day);
                    var count = map[mm_dd];
                    var color = "#ebedf0";
                    var title = mm_dd + "  No contributions";
                    if (count) {
                        color = getBackgroundColor(count);
                        title = mm_dd + "  " +count + " 次提交";
                    }
                    tmp_html += "<rect width='12' height='12' x='0' y='" + y_offset + "' fill='" + color + "'>" +
                        "<title>"+ title +"</title>" +
                        "</rect>";
                    day++;
                }
                start_html += tmp_html + "</g>";
                x_offset += 16;
            }
            start_html += "<g transform='translate(" + x_offset + ", 0)'>";
            tmp_html = "";
            for (var n = 0; n < 7; n++) {
                y_offset = n * 16;
                var mm_dd = getCurrent_MM_dd(month, day);
                var count = map[mm_dd];
                var color = "#ebedf0";
                var title = mm_dd + "  No contributions";
                if (count) {
                    color = getBackgroundColor(count);
                    title = mm_dd + "  " +count + " 次提交";
                }
                tmp_html += "<rect width='12' height='12' x='0' y='" + y_offset + "' fill='" + color + "'>" +
                    "<title>"+ title +"</title>" +
                    "</rect>";
                if (day == _daysInMonth) {
                    month = month + 1;
                    if (month == 13) {
                        break;
                    }
                    day = 0;
                    _daysInMonth = daysInMonth(2019, month);
                    isSetMonth = true;
                }
                day++;
            }
            start_html += tmp_html + "</g>";
            x_offset += 16;
            if (month == 13) {
                break;
            }
        }
        var html = start_html + end_html;
        $("#spot").append(html);
    });

    //获取方格背景色
    function getBackgroundColor(param) {
        if (param == "1") {
            return "#c6e48b";
        } else if (param == "2") {
            return "#7bc96f";
        } else if (param == "3") {
            return "#239a3b";
        } else {
            return "#196127";
        }
    }

    //获取MM-dd格式日期（month:0开始，week：1开始，tmpDay:0开始）
    function getMM_dd(month, week, tmpDay) {
        var mm_dd = "-";
        month = parseInt(month) + 1;
        tmpDay = parseInt(tmpDay) + 1;
        week = parseInt(week) - 1;
        if (month <= 9) {
            mm_dd = "0" + month + mm_dd;
        } else {
            mm_dd = month + mm_dd;
        }
        var day = week * 7 + tmpDay;
        if (day <= 9) {
            mm_dd = mm_dd + "0" + day;
        } else {
            mm_dd = mm_dd + day;
        }
        return mm_dd;
    }
    function getCurrent_MM_dd(month, day) {
        var mm_dd ="-";
        month = parseInt(month);
        if (month <= 9) {
            mm_dd = "0" + month + mm_dd;
        } else {
            mm_dd = month + mm_dd;
        }
        if (day <= 9) {
            mm_dd = mm_dd + "0" + day;
        } else {
            mm_dd = mm_dd + day;
        }
        return mm_dd;
    }
    //获取星期
    function getWeek(y,m,d){
        var date=new Date();
        date.setFullYear(y,m-1,d);
        var week = date.getDay()
        /*switch(week){
            case 0:
                return '星期日';
            case 1:
                return '星期一';
            case 2:
                return '星期二';
            case 3:
                return '星期三';
            case 4:
                return '星期四';
            case 5:
                return '星期五';
            case 6:
                return '星期六';
        }*/
        return week;
    }

    //获取某月天数
    function daysInMonth(year, month){
        return new Date(year, month, 0).getDate();
    }

    /*function getEnMonth(month) {
        switch(month) {
            case 1:
                return 'Jan';
            case 2:
                return 'Feb';
            case 3:
                return 'Mar';
            case 4:
                return 'Apr';
            case 5:
                return 'May';
            case 6:
                return 'Jun';
            case 7:
                return 'Jul';
            case 8:
                return 'Aug';
            case 9:
                return 'Sept';
            case 10:
                return 'Oct';
            case 11:
                return 'Nov';
            case 12:
                return 'Dec';
        }
    }*/

</script>
</body>

</html>
