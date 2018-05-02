angular.module('starter.controllers', [])
	.directive("hideTabs", function($rootScope) {
		restrict: "A" //attribute
		return {
			link: function(scope, jqLite, ab) {
				//scope 当前方法的作用域
				//jqLite 可以控制当前作用里面的所有属性
				//ab==>attributes 取到当前属性
				//console.log(this)
				//$ionicView.beforeEnter 离开当前页面进入新的页面所触发的事件
				scope.$on("$ionicView.beforeEnter", function() {
					scope.$watch(ab.hideTabs, function(value) {
						$rootScope.hideTabs = value
					})
				})
				//$ionicView.beforeLeave 从新的页面回到旧的页面所触发的事件
				scope.$on("$ionicView.beforeLeave", function() {
					$rootScope.hideTabs = false
				})
			}
		}
	})

	.controller('DashCtrl', function($scope,$state) {
        $("#onoffswitch_all").on('click', function(){
            clickSwitch_all();
        });
        var clickSwitch_all = function() {
        	var switch_machine = document.getElementById("onoffswitch_machine");
        	var switch_led1 = document.getElementById("onoffswitch_led1");
        	var switch_led2 = document.getElementById("onoffswitch_led2");
            var onOff;
            var instruction;
            if ($("#onoffswitch_all").is(':checked')) {
                onOff = "1";

                switch_machine.checked = true;
                switch_led1.checked = true;
                switch_led2.checked = true;
                
                instruction = "总开关开启";
            }
            else {
                onOff = "0";

                switch_machine.checked = false;
            	switch_led1.checked = false;
            	switch_led2.checked = false;
            	
            	instruction = "总开关关闭";
            }
            $.ajax({
                type: 'POST',
                url:'/control/all?onOff=' + onOff,
//                data: 'deviceId=' + onOff,
//                data: {"deviceId" : "1"},
//                dataType:"json",
                processData : false,
                contentType : false,
                error: function(result) {
                    console.log(result);
                },
                success: function(data) {
                    console.log(data);
                    if (data == 'SUCCESS') {
                    	var rowData;
                    	rowData = "<tr>";
                    	rowData += "<td style=\"text-align:center;color:green;\">" + "admin" + "</td>";
                    	rowData += "<td style=\"text-align:center;color:green;\">" + getNowFormatDate() + "</td>";
                    	rowData += "<td style=\"text-align:center;color:green;\">" + "总开关" + "</td>";
                    	rowData += "<td style=\"text-align:center;color:green;\">" + instruction + "</td>";
                    	rowData += "</tr>";
                    	$("#operate").append(rowData);
                    }
                }
            });
        };

        $("#onoffswitch_machine").on('click', function(){
            clickSwitch_machine();
        });
        var clickSwitch_machine = function() {
        	var switch_all = document.getElementById("onoffswitch_all");
        	var switch_led1 = document.getElementById("onoffswitch_led1");
        	var switch_led2 = document.getElementById("onoffswitch_led2");
            var onOff;
            var instruction;
            if ($("#onoffswitch_machine").is(':checked')) {
                onOff = "1";
                
                switch_all.checked = true;
                
                instruction = "开关打开";
            }
            else {
                onOff = "0";

                if ((switch_led1.checked==false) && (switch_led2.checked==false)) {
                	switch_all.checked = false;
                }
                
                instruction = "开关关闭";
            }
            $.ajax({
                type: 'POST',
                url:'/control/machine?onOff=' + onOff,
                data: 'onOff=' + onOff,
                processData : false,
                contentType : false,
                error: function(result) {
                    console.log(result);
                },
                success: function(data) {
                    console.log(data);
                    if (data == 'SUCCESS') {
                    	var rowData;
                    	rowData = "<tr>";
                    	rowData += "<td style=\"text-align:center;color:green;\">" + "admin" + "</td>";
                    	rowData += "<td style=\"text-align:center;color:green;\">" + getNowFormatDate() + "</td>";
                    	rowData += "<td style=\"text-align:center;color:green;\">" + "一体机" + "</td>";
                    	rowData += "<td style=\"text-align:center;color:green;\">" + instruction + "</td>";
                    	rowData += "</tr>";
                    	$("#operate").append(rowData);
                    }
                    else {
                    }
                }
            });
        };

        $("#machineSig").on('click', function(){
            machineSwitchSig();
        });
        var machineSwitchSig = function() {
        	var sigSource;
        	var instruction = "切换信号源到";
            var objSig = document.getElementsByName("radio");
            for (var i=0; i<objSig.length; i++) {
                if (objSig[i].checked) {
                    sigSource = objSig[i].value;
                }
            }
            switch (sigSource) {
            case "1":
            	instruction += "主页";
            	break;
            case "2":
            	instruction += "HDMI2.0";
            	break;
            case "3":
            	instruction += "HDMI1.4";
            	break;
            case "4":
            	instruction += "内置电脑";
            	break;
            default:
            	break;
            }

            $.ajax({
                type: 'POST',
                url:'/control/machineSig?sigSource=' + sigSource,
//                data: 'onOff=' + onOff,
                processData : false,
                contentType : false,
                error: function(result) {
                    console.log(result);
                },
                success: function(data) {
                    console.log(data);
                    if (data == 'SUCCESS') {
                    	var rowData;
                    	rowData = "<tr>";
                    	rowData += "<td style=\"text-align:center;color:green;\">" + "admin" + "</td>";
                    	rowData += "<td style=\"text-align:center;color:green;\">" + getNowFormatDate() + "</td>";
                    	rowData += "<td style=\"text-align:center;color:green;\">" + "一体机" + "</td>";
                    	rowData += "<td style=\"text-align:center;color:green;\">" + instruction + "</td>";
                    	rowData += "</tr>";
                    	$("#operate").append(rowData);
                    }
                    else {
                    }
                }
            });
        };

        $("#switchIncrease").on('click', function(){
            machineSwitchIncrease();
        });
        var machineSwitchIncrease = function() {  
            $.ajax({
                type: 'POST',
                url:'/control/machineVol?volume=1',
//                data: 'onOff=' + onOff,
                processData : false,
                contentType : false,
                error: function(result) {
                    console.log(result);
                },
                success: function(data) {
                    console.log(data);
                    if (data == 'SUCCESS') {
                    	var volume = document.getElementById("volume");
                    	volume.value += 1;
                    	
                    	var rowData;
                    	rowData = "<tr>";
                    	rowData += "<td style=\"text-align:center;color:green;\">" + "admin" + "</td>";
                    	rowData += "<td style=\"text-align:center;color:green;\">" + getNowFormatDate() + "</td>";
                    	rowData += "<td style=\"text-align:center;color:green;\">" + "一体机" + "</td>";
                    	rowData += "<td style=\"text-align:center;color:green;\">" + "音量增加" + "</td>";
                    	rowData += "</tr>";
                    	$("#operate").append(rowData);
                    }
                    else {
                    }
                }
            });
        };

        $("#switchDecrease").on('click', function(){
            machineSwitchDecrease();
        });
        var machineSwitchDecrease = function() {            
            $.ajax({
                type: 'POST',
                url:'/control/machineVol?volume=0',
//                data: 'onOff=' + onOff,
                processData : false,
                contentType : false,
                error: function(result) {
                    console.log(result);
                },
                success: function(data) {
                    console.log(data);
                    if (data == 'SUCCESS') {
                    	var volume = document.getElementById("volume");
                    	volume.value -= 1;
                    	
                    	var rowData;
                    	rowData = "<tr>";
                    	rowData += "<td style=\"text-align:center;color:green;\">" + "admin" + "</td>";
                    	rowData += "<td style=\"text-align:center;color:green;\">" + getNowFormatDate() + "</td>";
                    	rowData += "<td style=\"text-align:center;color:green;\">" + "一体机" + "</td>";
                    	rowData += "<td style=\"text-align:center;color:green;\">" + "音量减少" + "</td>";
                    	rowData += "</tr>";
                    	$("#operate").append(rowData);
                    }
                    else {
                    }
                }
            });
        };

        $("#onoffswitch_led1").on('click', function(){
            clickSwitch_led1();
        });
        var clickSwitch_led1 = function() {
        	var switch_all = document.getElementById("onoffswitch_all");
        	var switch_machine = document.getElementById("onoffswitch_machine");
        	var switch_led2 = document.getElementById("onoffswitch_led2");

            var onOff;
            var instruction;
            if ($("#onoffswitch_led1").is(':checked')) {
                onOff = "1";
                switch_all.checked = true;
                
                instruction = "开关打开";
            }
            else {
                onOff = "0";
                console.log(switch_machine);
                console.log(switch_led2);
                if ((switch_machine.checked==false) && (switch_led2.checked==false)) {
                	switch_all.checked = false;
                }
                
                instruction = "开关关闭";
            }
            $.ajax({
                type: 'POST',
                url:'/control/led1?onOff=' + onOff,
                data: 'onOff=' + onOff,
                processData : false,
                contentType : false,
                error: function(result) {
                    console.log(result);
                },
                success: function(data) {
                    console.log(data);
                    if (data == 'SUCCESS') {
                    	var rowData;
                    	rowData = "<tr>";
                    	rowData += "<td style=\"text-align:center;color:green;\">" + "admin" + "</td>";
                    	rowData += "<td style=\"text-align:center;color:green;\">" + getNowFormatDate() + "</td>";
                    	rowData += "<td style=\"text-align:center;color:green;\">" + "智能灯一" + "</td>";
                    	rowData += "<td style=\"text-align:center;color:green;\">" + instruction + "</td>";
                    	rowData += "</tr>";
                    	$("#operate").append(rowData);
                    }
                    else {
                    }
                }
            });
        };

        $("#onoffswitch_led2").on('click', function(){
            clickSwitch_led2();
        });
        var clickSwitch_led2 = function() {
            var onOff;
            var instruction;
            
            var switch_all = document.getElementById("onoffswitch_all");
        	var switch_machine = document.getElementById("onoffswitch_machine");
        	var switch_led1 = document.getElementById("onoffswitch_led1");

            if ($("#onoffswitch_led2").is(':checked')) {
                onOff = "1";
                
                switch_all.checked = true;
                
                instruction = "开关打开";
            }
            else {
                onOff = "0";
                
                if ((switch_machine.checked==false) && (switch_led1.checked==false)) {
                	switch_all.checked = false;
                }
                
                instruction = "开关关闭";
            }
            $.ajax({
                type: 'POST',
                url:'/control/led2?onOff=' + onOff,
                data: 'onOff=' + onOff,
                processData : false,
                contentType : false,
                error: function(result) {
                    console.log(result);
                },
                success: function(data) {
                    console.log(data);
                    if (data == 'SUCCESS') {
                    	var rowData;
                    	rowData = "<tr>";
                    	rowData += "<td style=\"text-align:center;color:green;\">" + "admin" + "</td>";
                    	rowData += "<td style=\"text-align:center;color:green;\">" + getNowFormatDate() + "</td>";
                    	rowData += "<td style=\"text-align:center;color:green;\">" + "智能灯二" + "</td>";
                    	rowData += "<td style=\"text-align:center;color:green;\">" + instruction + "</td>";
                    	rowData += "</tr>";
                    	$("#operate").append(rowData);
                    }
                    else {
                    }
                }
            });
        };

        $("#sensorDataSwitch").on('click', function(){
            sensorDataSwitch();
        });
        var sensorDataSwitch = function() {
        	var sensorType;
        	var title;
            var objSig = document.getElementsByName("sensorTypeRadio");
            for (var i=0; i<objSig.length; i++) {
                if (objSig[i].checked) {
                	sensorType = objSig[i].value;
                }
            }
            if (sensorType == "pm25") {
            	title = "PM2.5";
            }
            else if (sensorType == "co2") {
            	title = "CO2";
            }
            else if (sensorType == "tvoc") {
            	title = "TVOC";
            }
            else if (sensorType == "temperature") {
            	title = "温度";
            }
            else if (sensorType == "humidity") {
            	title = "湿度";
            }
            else if (sensorType == "formaldehyde") {
            	title = "甲醛";
            }
            else {
            	
            }

            $.ajax({
                type: 'POST',
                url:'/control/sensor/history?type=' + sensorType,
                processData : false,
                contentType : false,
                error: function(result) {
                    console.log(result);
                },
                success: function(data) {
                    console.log(data);

                    var value = [];
                    for (var i=0; i<data.length; i++) {
                    	value.push(data[i]);
                    }
                    console.log(value);
                    DrawSensor('canvasDiv5', title, value);
                }
            });
        };

		$('.daohang a').click(function(e) {
	        e.preventDefault();
	        $('.daohang a').removeClass("current")
	        $(this).addClass("current")
	        $(".tab").removeClass("show");
	        $('#' + $(this).attr('title')).addClass('show');
	    });

	    setInterval(function () {
	    	var table = document.getElementsByName("sensor");
	    	var cells;
            
	    	$.ajax({
                type: 'POST',
                url:'/control/sensor',
                processData : false,
                contentType : false,
                async:false,
                error: function(result) {
                    console.log(result);
                },
                success: function(data) {
                    console.log(data);
                    var result = data.split(",");
                    var pm25 = result[0].split('.');
                    DrawPm25('canvasDiv1', pm25[0]);
                    DrawPm25('canvasDiv2', pm25[0]);
                    DrawPm25('canvasDiv3', pm25[0]);
                    DrawPm25('canvasDiv4', pm25[0]);
        	        
                    for (var i=0; i<table.length; i++) {
                    	cells = table[i].rows[1].cells;
                    	var co2 = result[1].split('.');
                    	cells[0].innerHTML = co2[0];
                    	cells[1].innerHTML = result[2];
                    	var temp = result[3].substring(0, result[3].length-2);
                    	cells[2].innerHTML = temp;
                    	var hum = result[4].substring(0, result[4].length-2);
                    	cells[3].innerHTML = hum;
                    	cells[4].innerHTML = result[5];
                    }

                    if (data == 'SUCCESS') {
                    }
                    else {
                    }
                }
            });
	    }, 15000);

	    $(document).ready(function() {
        	$.ajax({
                type: 'POST',
                url:'/control/status',
//                data: 'deviceId=' + onOff,
//                data: {"deviceId" : "1"},
//                dataType:"json",
                processData : false,
                contentType : false,
                async:false,
                error: function(result) {
                    console.log(result);
                    alert(result);
                },
                success: function(data) {
                    console.log(data);
                    var result = data.split(",");
                    var switchAll = document.getElementById("onoffswitch_all");
                    var switchMachine = document.getElementById("onoffswitch_machine");
                    var machineSig = document.getElementsByName("radio");
                    var machineVolume = document.getElementById("volume");
                    var switchLed1 = document.getElementById("onoffswitch_led1");
                    var switchLed2 = document.getElementById("onoffswitch_led2");

                    if ((result[0] == "1") || (result[3] == "1") || (result[4] == "1")) {
                    	switchAll.checked = true;
                    }
                    else {
                    	switchAll.checked = false;
                    }
                    switchMachine.checked = (result[0] == "1") ? true : false;
                    for (var i=0; i<machineSig.length; i++) {
                        if (machineSig[i].value == result[1]) {
                        	console.log(i + "---");
                        	machineSig[i].checked = true;
                        	break;
                        }
                    }
                    machineVolume.value = result[2];
                    switchLed1.checked = (result[3] == "1") ? true : false;
                    switchLed2.checked = (result[4] == "1") ? true : false;

                    if (data == 'SUCCESS') {
                    } else {
                    }
                }
            });
        	
        	var table = document.getElementsByName("sensor");
        	var cells;
        	$.ajax({
                type: 'POST',
                url:'/control/sensor',
                processData : false,
                contentType : false,
                async:false,
                error: function(result) {
                    console.log(result);
                },
                success: function(data) {
                    console.log(data);
                    var result = data.split(",");
                    var pm25 = result[0].split('.');
                    DrawPm25('canvasDiv1', pm25[0]);
                    DrawPm25('canvasDiv2', pm25[0]);
                    DrawPm25('canvasDiv3', pm25[0]);
                    DrawPm25('canvasDiv4', pm25[0]);
                    
                    var value = [9,1,12,20,26,30,32,29,22,12,20,6,3,1,12,20,26,30,32,29,22,12,0,6];
                    DrawSensor('canvasDiv5', 'PM2.5', value);

                    for (var i=0; i<table.length; i++) {
                    	cells = table[i].rows[1].cells;
                    	var co2 = result[1].split('.');
                    	cells[0].innerHTML = co2[0];
                    	cells[1].innerHTML = result[2];
                    	var temp = result[3].substring(0, result[3].length-2);
                    	cells[2].innerHTML = temp;
                    	var hum = result[4].substring(0, result[4].length-2);
                    	cells[3].innerHTML = hum;
                    	cells[4].innerHTML = result[5];
                    }

                    if (data == 'SUCCESS') {
                    } else {
                    }
                }
            });
        	$.ajax({
                type: 'POST',
                url:'/control/operate',
                processData : false,
                contentType : false,
                async:false,
                error: function(result) {
                    console.log(result);
                },
                success: function(data) {
                	var rowData;
                	for (var i=0; i<data.length; i++) {
                    	rowData = "<tr>";
                    	rowData += "<td style=\"text-align:center;color:green;\">" + data[i].user + "</td>";
                    	rowData += "<td style=\"text-align:center;color:green;\">" + data[i].operate_time + "</td>";
                    	rowData += "<td style=\"text-align:center;color:green;\">" + data[i].device_id + "</td>";
                    	rowData += "<td style=\"text-align:center;color:green;\">" + data[i].instruction + "</td>";
                    	rowData += "</tr>";
                    	$("#operate").append(rowData);
                	}
                }
        	});
        });
	})

	.controller('Room1Ctrl', function($scope,$state) {
		$scope.gonext = function () {
            $state.go('tab.dash');
        }
	})

	function getNowFormatDate() {
	    var date = new Date();
	    var seperator1 = "-";
	    var seperator2 = ":";
	    var month = date.getMonth() + 1;
	    var strDate = date.getDate();
	    if (month >= 1 && month <= 9) {
	        month = "0" + month;
	    }
	    if (strDate >= 0 && strDate <= 9) {
	        strDate = "0" + strDate;
	    }
	    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
	            + " " + date.getHours() + seperator2 + date.getMinutes()
	            + seperator2 + date.getSeconds();
	    return currentdate;
	}

	function DrawPm25(render, value) {
		var Width = 370
		var Height = 240
		var Fontsize = 14
		var screenwidth = document.body.clientWidth
		if (screenwidth >= 970) {
			Width = 500
			Height = 300
			Fontsize = 20
		}
		else if (screenwidth<970 && screenwidth>750) {
			Fontsize = 16
		}
		else {
			Width = 300;
			Height = 200;
			Fontsize = 12
		}

		var text = 'PM 2.5\n' + value + '(μg/m3)';

		var data = [
            {name : '优',value : 25,color:'#008B00'},
            {name : '良',value : 15,color:'#EEEE00'},
            {name : '轻度污染',value : 15,color:'#CD950C'},
            {name : '中度污染',value : 15,color:'#CD0000'},
            {name : '重度污染',value : 15,color:'#8E388E'},
            {name : '严重污染',value : 15,color:'#8B4513'}
        ];

        var chart = new iChart.Donut2D({
            render : render,
            center:{
                text:text,
                shadow:true,
                shadow_offsetx:0,
                shadow_offsety:2,
                shadow_blur:2,
                shadow_color:'#b7b7b7',
                color:'#008B00',
                fontsize:Fontsize
            },
            data: data,
            offsetx:-60,
            shadow:true,
            background_color:'white',
            separate_angle:0,//分离角度
            border:0,
            tip:{
                enable:true,
                showType:'fixed'
            },
            legend : {
                enable : true,
                shadow:true,
                background_color:null,
                border:true,
                legend_space:30,//图例间距
                line_height:24,//设置行高
                sign_space:10,//小图标与文本间距
                sign_size:20,//小图标大小
                color:'#6f6f6f',
                fontsize:Fontsize//文本大小
            },
            sub_option:{
                label:false,
                color_factor : 0.3
            },
            showpercent:true,
            decimalsnum:2,
            width : Width,
            height : Height,
            radius:140
        });

        /**
         *利用自定义组件构造左侧说明文本。
         */
        chart.plugin(new iChart.Custom({
            drawFn:function(){
                /**
                 *计算位置
                 */
                var y = chart.get('originy');
                /**
                 *在左侧的位置，设置竖排模式渲染文字。
                 */
                chart.target.textAlign('center')
                    .textBaseline('middle')
                    .textFont('600 24px 微软雅黑')
                    .fillText('',100,y,false,'#6d869f', 'tb',26,false,0,'middle');

            }
        }));

        chart.draw();
	}

	function DrawSensor(render, type, value) {
		var Width = 370
		var Height = 240
		var Fontsize = 14
		var screenwidth = document.body.clientWidth
		if (screenwidth >= 970) {
			Width = 500
			Height = 300
			Fontsize = 20
		}
		else if (screenwidth<970 && screenwidth>750) {
			Fontsize = 16
		}
		else {
			Width = 300;
			Height = 200;
			Fontsize = 12
		}

		var data = [
	        {
	            name : '北京',
	            value:value,
	            color:'#1f7e92',
	            line_width:3
	        }
	    ];
	    var chart = new iChart.LineBasic2D({
	        render : render,
	        data: data,
	        title : type,
	        width : 800,
	        height : 400,
	        coordinate:{height:'90%',background_color:'#f6f9fa'},
	        sub_option:{
	            hollow_inside:false,//设置一个点的亮色在外环的效果
	            point_size:Fontsize
	        },
	        labels:["1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24"]
	    });
	    chart.draw();
	}

