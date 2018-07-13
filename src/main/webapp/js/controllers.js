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
    var totalPage = 1;
    var curPageNo = 1;

    //websocket
    var websocket = null;
    if ('WebSocket' in window) {
        websocket = new WebSocket("ws://192.168.1.150:8080/igrsiot/websocket");
    }
    else {
        console.log('browser not support websocket')
    }
    websocket.onerror = function () {
    };
    websocket.onopen = function () {
    };
    websocket.onmessage = function (event) {
        console.log(event.data);
        var data = event.data;
        var str;
        if (data.search("pw::") !== -1) {      //purifier data from server
            // str = data.substring(data.indexOf("pw::"));
            var result;
            var results = data.split(",");
            for (var i=0; i<results.length; i++) {
                result = results[i].split("::");
                switch (result[0]) {
                    case 'pw':
                        var switch_purifier = document.getElementById("onoffswitch_purifier");
                        switch_purifier.checked = result[1] === '10';
                        break;
                    case 'lc':
                        var check = document.getElementById("purifierLcCheck");
                        check.checked = result[1] === "10";
                        break;
                    case 'sl':
                        var check = document.getElementById("purifierSlCheck");
                        check.checked = result[1] === "10";
                        break;
                    case 'mo':
                        var check = document.getElementById("purifierMoCheck");
                        check.checked = result[1] === "10";
                        break;
                    case 'io':
                        var check = document.getElementById("purifierIoCheck");
                        check.checked = result[1] === "10";
                        break;
                    case 'uv':
                        var check = document.getElementById("purifierUvCheck");
                        check.checked = result[1] === "10";
                        break;
                    case 'ti':
                        var check = document.getElementById("purifierTiCheck");
                        check.checked = result[1] !== "000";
                        break;
                    case 'fa':
                        var value = result[1] + '0';
                        var radio = document.getElementsByName("purifierRadio");
                        for (var j=0; j<radio.length; j++) {
                            if (radio[j].value === value) {
                                radio[j].checked = true;
                                break;
                            }
                        }
                        break;
                }
            }
        }
        else if (data.search("pm25") !== -1) {     //sensor data from server
            results = data.split(",");
            for (var i=0; i<results.length; i++) {
                result = results[i].split(":");
                switch (result[0]) {
                    case 'pm25':
                        console.log(result[1]);
                        var pm25 = result[1].split('.');
                        DrawPm25('canvasDiv1', pm25[0]);
                        DrawPm25('canvasDiv2', pm25[0]);
                        DrawPm25('canvasDiv3', pm25[0]);
                        DrawPm25('canvasDiv4', pm25[0]);
                        DrawPm25('canvasDiv6', pm25[0]);
                        break;
                    case 'co2':
                        var co2 = result[1].split('.');
                        var co2Value = document.getElementsByName("co2Value");
                        for (var j=0; j<co2Value.length; j++) {
                            co2Value[j].innerText = co2[0];
                        }
                        break;
                    case 'tvoc':
                        var tvocValue = document.getElementsByName("tvocValue");
                        for (var j=0; j<tvocValue.length; j++) {
                            tvocValue[j].innerText = parseFloat(result[1]).toFixed(3);
                        }
                        break;
                    case 'temperature':
                        var temp = parseFloat(result[1]).toFixed(1);
                        var temperatureValue = document.getElementsByName("temperatureValue");
                        for (var j=0; j<temperatureValue.length; j++) {
                            temperatureValue[j].innerText = temp;
                        }
                        break;
                    case 'humidity':
                        var hum = parseFloat(result[1]).toFixed(1);
                        var humidityValue = document.getElementsByName("humidityValue");
                        for (var j=0; j<humidityValue.length; j++) {
                            humidityValue[j].innerText = hum;
                        }
                        break;
                    case 'formaldehyde':
                        var formaldehydeValue = document.getElementsByName("formaldehydeValue");
                        for (var j=0; j<formaldehydeValue.length; j++) {
                            formaldehydeValue[j].innerText = parseFloat(result[1]).toFixed(3);
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        else {      //sync message from other client or socket controller
            str = data.split(":");
            switch (str[0]) {
                case 'welcomeModeSwitch':
                    var switch_welcomemode = document.getElementById("onoffswitch_welcomemode");
                    switch_welcomemode.checked = str[1] === "1";
                    break;
                case 'allSwitch':
                    var value = str[1] === "1";
                    var switch_all = document.getElementById("onoffswitch_all");
                    var switch_machine1 = document.getElementById("onoffswitch_machine1");
                    var switch_machine2 = document.getElementById("onoffswitch_machine2");
                    var switch_led1 = document.getElementById("onoffswitch_led1");
                    var switch_led2 = document.getElementById("onoffswitch_led2");
                    var switch_curtain = document.getElementById("onoffswitch_curtain");
                    switch_all.checked = value;
                    switch_machine1.checked = value;
                    switch_machine2.checked = value;
                    switch_led1.checked = value;
                    switch_led2.checked = value;
                    switch_curtain.checked = value;
                    break;
                case 'machine1Switch':
                    var switch_machine1 = document.getElementById("onoffswitch_machine1");
                    switch_machine1.checked = str[1] === "1";
                    break;
                case 'machine1Sig':
                    var objSig = document.getElementsByName("machine1Radio");
                    for (var i=0; i<objSig.length; i++) {
                        if (objSig[i].value == str[1]) {
                            objSig[i].checked = true;
                        }
                    }
                    break;
                case 'machine1Volume':
                    var volume = document.getElementById("machine1Volume");
                    if (str[1] == '1') {
                        volume.value += 1;
                    }
                    else {
                        volume.value -= 1;
                    }
                    break;
                case 'machine2Switch':
                    var switch_machine2 = document.getElementById("onoffswitch_machine2");
                    switch_machine2.checked = str[1] === "1";
                    break;
                case 'machine2Sig':
                    var objSig = document.getElementsByName("machine2Radio");
                    for (var i=0; i<objSig.length; i++) {
                        if (objSig[i].value == str[1]) {
                            objSig[i].checked = true;
                        }
                    }
                    break;
                case 'machine2Volume':
                    var volume = document.getElementById("machine2Volume");
                    if (str[1] == '1') {
                        volume.value += 1;
                    }
                    else {
                        volume.value -= 1;
                    }
                    break;
                case 'led1Switch':
                    var switch_led1 = document.getElementById("onoffswitch_led1");
                    switch_led1.checked = str[1] === "1";
                    break;
                case 'led2Switch':
                    var switch_led2 = document.getElementById("onoffswitch_led2");
                    switch_led2.checked = str[1] === "1";
                    break;
                case 'curtainSwitch':
                    var switch_curtain = document.getElementById("onoffswitch_curtain");
                    switch_curtain.checked = str[1] === "1";
                    break;
                case 'purifierSwitch':
                    var switch_purifier = document.getElementById("onoffswitch_purifier");
                    switch_purifier.checked = str[1] === "1";
                    break;
               default:
                    break;
            }
        }
    };
    websocket.onclose = function () {
    };
    window.onbeforeunload = function () {
        webSocketClose();
    };
    function webSocketClose() {
        websocket.close();
    }
    function webSocketsendMessage(message) {
        websocket.send(message);
    }

    $scope.onSwitchWelcomeModeClick = function () {
        var switchWelcome = document.getElementById("onoffswitch_welcomemode");
        var onOff;
        var instruction;

        if (!switchWelcome.checked) {
            onOff = "1";
            instruction = "迎宾模式开启";
        }
        else {
            onOff = "0";
            instruction = "迎宾模式关闭";
        }

        $.ajax({
            type: 'POST',
            url:'/igrsiot/control/welcomemode',
            data:{
                onOff:onOff
            },
            contentType:'application/x-www-form-urlencoded; charset=utf-8',
            async:true,
            error: function(result) {
            },
            success: function(data) {
                console.log(data);
                var msg = "welcomeModeSwitch" + ":" + onOff;
                webSocketsendMessage(msg);
                if (data == 'SUCCESS') {
                }
            }
        });
    };

    $scope.onSwitchAllClick = function() {
        var switch_all = document.getElementById("onoffswitch_all");
        var switch_machine1 = document.getElementById("onoffswitch_machine1");
        var switch_machine2 = document.getElementById("onoffswitch_machine2");
        var switch_led1 = document.getElementById("onoffswitch_led1");
        var switch_led2 = document.getElementById("onoffswitch_led2");
        var switch_curtain = document.getElementById("onoffswitch_curtain");
        var onOff;
        var instruction;
        if (!switch_all.checked) {
            switch_machine1.checked = true;
            switch_machine2.checked = true;
            switch_led1.checked = true;
            switch_led2.checked = true;
            switch_curtain.checked = true;
            onOff = "1";

            instruction = "总开关开启";
        }
        else {
            switch_machine1.checked = false;
            switch_machine2.checked = false;
            switch_led1.checked = false;
            switch_led2.checked = false;
            switch_curtain.checked = false;
            onOff = "0";

            instruction = "总开关关闭";
        }
        $.ajax({
            type: 'POST',
            url:'/igrsiot/control/all',
            data:{
                onOff:onOff
            },
            // dataType:'json',
            contentType:'application/x-www-form-urlencoded; charset=utf-8',
            async:true,
            error: function(result) {
            },
            success: function(data) {
                console.log(data);
                var msg = "allSwitch" + ":" + onOff;
                webSocketsendMessage(msg);
                if (data == 'SUCCESS') {
                }
            }
        });
    };

    $scope.onSwitchMachine1Click = function() {
        var switch_all = document.getElementById("onoffswitch_all");
        var switch_machine1 = document.getElementById("onoffswitch_machine1");
        var switch_machine2 = document.getElementById("onoffswitch_machine2");
        var switch_led1 = document.getElementById("onoffswitch_led1");
        var switch_led2 = document.getElementById("onoffswitch_led2");
        var switch_curtain = document.getElementById("onoffswitch_curtain");
        var index = 0;
        var onOff;
        var instruction;
        if (!switch_machine1.checked) {
            onOff = "1";

            switch_all.checked = true;

            instruction = "开关打开";
        }
        else {
            onOff = "0";

            if ((!switch_machine2.checked) && (!switch_led1.checked) && (!switch_led2.checked) && (!switch_curtain.checked)) {
                switch_all.checked = false;
            }

            instruction = "开关关闭";
        }
        $.ajax({
            type: 'POST',
            url:'/igrsiot/control/machine',
            data:{
                room:'100',
                index:index,
                onOff:onOff
            },
            // dataType:'json',
            contentType:'application/x-www-form-urlencoded; charset=utf-8',
            async:true,
            error: function(result) {
            },
            success: function(data) {
                console.log(data);
                var msg = "machine1Switch" + ":" + onOff;
                webSocketsendMessage(msg);
                if (data == 'SUCCESS') {
                }
                else {
                }
            }
        });
    };

    $scope.machine1Switch = function () {
        var index = 1;
        var sigSource;
        var instruction = "切换信号源到";
        var objSig = document.getElementsByName("machine1Radio");
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
            instruction += "HDMI1";
            break;
        case "3":
            instruction += "HDMI2";
            break;
        case "4":
            instruction += "内置电脑";
            break;
        default:
            break;
        }

        $.ajax({
            type: 'POST',
            url:'/igrsiot/control/machineSig',
            data:{
                index:index,
                sigSource:sigSource
            },
            // dataType:'json',
            contentType:'application/x-www-form-urlencoded; charset=utf-8',
            async:true,
            error: function(result) {
            },
            success: function(data) {
                console.log(data);
                var msg = "machine1Sig" + ":" + sigSource;
                webSocketsendMessage(msg);
                if (data == 'SUCCESS') {
                }
                else {
                }
            }
        });
    };

    $scope.machine1SwitchIncrease = function() {
        $.ajax({
            type: 'POST',
            url:'/igrsiot/control/machineVol',
            data:{
                index:1,
                volume:1
            },
            // dataType:'json',
            contentType:'application/x-www-form-urlencoded; charset=utf-8',
            async:true,
            error: function(result) {
            },
            success: function(data) {
                console.log(data);
                if (data == 'SUCCESS') {
                    var volume = document.getElementById("machine1Volume");
                    volume.value += 1;
                    var msg = "machine1Volume" + ":" + volume;
                    webSocketsendMessage(msg);
                }
                else {
                }
            }
        });
    };

    $scope.machine1SwitchDecrease = function() {
        $.ajax({
            type: 'POST',
            url:'/igrsiot/control/machineVol',
            data:{
                index:1,
                volume:0
            },
            // dataType:'json',
            contentType:'application/x-www-form-urlencoded; charset=utf-8',
            async:true,
            error: function(result) {
            },
            success: function(data) {
                console.log(data);
                if (data == 'SUCCESS') {
                    var volume = document.getElementById("machine1Volume");
                    volume.value -= 1;
                    var msg = "machine1Volume" + ":" + volume;
                    webSocketsendMessage(msg);
                }
                else {
                }
            }
        });
    };

    $scope.onSwitchMachine2Click = function() {
        var switch_all = document.getElementById("onoffswitch_all");
        var switch_machine1 = document.getElementById("onoffswitch_machine1");
        var switch_machine2 = document.getElementById("onoffswitch_machine2");
        var switch_led1 = document.getElementById("onoffswitch_led1");
        var switch_led2 = document.getElementById("onoffswitch_led2");
        var switch_curtain = document.getElementById("onoffswitch_curtain");
        var onOff;
        var instruction;
        if (!switch_machine2.checked) {
            onOff = "1";

            switch_all.checked = true;

            instruction = "开关打开";
        }
        else {
            onOff = "0";

            if ((!switch_machine1.checked) && (!switch_led1.checked) && (!switch_led2.checked) && (!switch_curtain.checked)) {
                switch_all.checked = false;
            }

            instruction = "开关关闭";
        }
        $.ajax({
            type: 'POST',
            url:'/igrsiot/control/machine',
            data:{
                index:2,
                onOff:onOff
            },
            // dataType:'json',
            contentType:'application/x-www-form-urlencoded; charset=utf-8',
            async:true,
            error: function(result) {
            },
            success: function(data) {
                console.log(data);
                var msg = "machine2Switch" + ":" + onOff;
                webSocketsendMessage(msg);
                if (data == 'SUCCESS') {
                }
                else {
                }
            }
        });
    };

    $scope.machine2Switch = function() {
        var sigSource;
        var instruction = "切换信号源到";
        var objSig = document.getElementsByName("machine2Radio");
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
                instruction += "HDMI1";
                break;
            case "3":
                instruction += "HDMI2";
                break;
            case "4":
                instruction += "内置电脑";
                break;
            default:
                break;
        }

        $.ajax({
            type: 'POST',
            url:'/igrsiot/control/machineSig',
            data:{
                index:2,
                sigSource:sigSource
            },
            // dataType:'json',
            contentType:'application/x-www-form-urlencoded; charset=utf-8',
            async:true,
            error: function(result) {
            },
            success: function(data) {
                console.log(data);
                var msg = "machine2Sig" + ":" + sigSource;
                webSocketsendMessage(msg);
                if (data == 'SUCCESS') {
                }
                else {
                }
            }
        });
    };

    $scope.machine2SwitchIncrease = function() {
        $.ajax({
            type: 'POST',
            url:'/igrsiot/control/machineVol',
            data:{
                index:2,
                volume:1
            },
            // dataType:'json',
            contentType:'application/x-www-form-urlencoded; charset=utf-8',
            async:true,
            error: function(result) {
            },
            success: function(data) {
                console.log(data);
                if (data == 'SUCCESS') {
                    var volume = document.getElementById("machine2Volume");
                    volume.value += 1;
                    var msg = "machine2Volume" + ":" + volume;
                    webSocketsendMessage(msg);
                }
                else {
                }
            }
        });
    };

    $scope.machine2SwitchDecrease = function() {
        $.ajax({
            type: 'POST',
            url:'/igrsiot/control/machineVol',
            data:{
                index:2,
                volume:0
            },
            // dataType:'json',
            contentType:'application/x-www-form-urlencoded; charset=utf-8',
            async:true,
            error: function(result) {
            },
            success: function(data) {
                console.log(data);
                if (data == 'SUCCESS') {
                    var volume = document.getElementById("machine2Volume");
                    volume.value -= 1;
                    var msg = "machine2Volume" + ":" + volume;
                    webSocketsendMessage(msg);
                }
                else {
                }
            }
        });
    };

    $scope.onSwitchLed1Click = function() {
        var switch_all = document.getElementById("onoffswitch_all");
        var switch_machine1 = document.getElementById("onoffswitch_machine1");
        var switch_machine2 = document.getElementById("onoffswitch_machine2");
        var switch_led1 = document.getElementById("onoffswitch_led1");
        var switch_led2 = document.getElementById("onoffswitch_led2");
        var switch_curtain = document.getElementById("onoffswitch_curtain");
        var index = 1;

        var onOff;
        var instruction;
        if (!switch_led1.checked) {
            onOff = "1";
            switch_all.checked = true;

            instruction = "开关打开";
        }
        else {
            onOff = "0";
            if ((!switch_machine1.checked) && (!switch_machine2.checked) && (!switch_led2.checked) && (!switch_curtain.checked)) {
                switch_all.checked = false;
            }

            instruction = "开关关闭";
        }
        $.ajax({
            type: 'POST',
            url:'/igrsiot/control/led',
            data:{
                index:index,
                onOff:onOff
            },
            // dataType:'json',
            contentType:'application/x-www-form-urlencoded; charset=utf-8',
            async:true,
            error: function(result) {
            },
            success: function(data) {
                console.log(data);
                var msg = "led1Switch" + ":" + onOff;
                webSocketsendMessage(msg);
                if (data == 'SUCCESS') {
                }
                else {
                }
            }
        });
    };

    $scope.onSwitchLed2Click = function() {
        var switch_all = document.getElementById("onoffswitch_all");
        var switch_machine1 = document.getElementById("onoffswitch_machine1");
        var switch_machine2 = document.getElementById("onoffswitch_machine2");
        var switch_led1 = document.getElementById("onoffswitch_led1");
        var switch_led2 = document.getElementById("onoffswitch_led2");
        var switch_curtain = document.getElementById("onoffswitch_curtain");
        var index = 2;

        var onOff;
        var instruction;
        if (!switch_led2.checked) {
            onOff = "1";

            switch_all.checked = true;

            instruction = "开关打开";
        }
        else {
            onOff = "0";

            if ((!switch_machine1.checked) && (!switch_machine2.checked) && (!switch_led1.checked) && (!switch_curtain.checked)) {
                switch_all.checked = false;
            }

            instruction = "开关关闭";
        }
        $.ajax({
            type: 'POST',
            url:'/igrsiot/control/led',
            data:{
                index:index,
                onOff:onOff
            },
            // dataType:'json',
            contentType:'application/x-www-form-urlencoded; charset=utf-8',
            async:true,
            error: function(result) {
            },
            success: function(data) {
                console.log(data);
                var msg = "led2Switch" + ":" + onOff;
                webSocketsendMessage(msg);
                if (data == 'SUCCESS') {
                }
                else {
                }
            }
        });
    };

    $scope.onSwitchCurtainClick = function() {
        var switch_all = document.getElementById("onoffswitch_all");
        var switch_machine1 = document.getElementById("onoffswitch_machine1");
        var switch_machine2 = document.getElementById("onoffswitch_machine2");
        var switch_led1 = document.getElementById("onoffswitch_led1");
        var switch_led2 = document.getElementById("onoffswitch_led2");
        var switch_curtain = document.getElementById("onoffswitch_curtain");
        // var onOff;
        // var instruction;
        if (!switch_curtain.checked) {
            onOff = "1";

            switch_all.checked = true;

            instruction = "开关打开";
        }
        else {
            onOff = "0";

            if ((!switch_machine1.checked) && (!switch_machine2.checked) && (!switch_led1.checked) && (!switch_led2.checked)) {
                switch_all.checked = false;
            }

            instruction = "开关关闭";
        }
        $.ajax({
            type: 'POST',
            url:'/igrsiot/control/curtain',
            data:{
                onOff:onOff
            },
            // dataType:'json',
            contentType:'application/x-www-form-urlencoded; charset=utf-8',
            async:true,
            error: function(result) {
            },
            success: function(data) {
                console.log(data);
                var msg = "curtainSwitch" + ":" + onOff;
                webSocketsendMessage(msg);
                if (data == 'SUCCESS') {
                }
                else {
                }
            }
        });
    };

    $scope.onSwitchPurifierClick = function() {
        var onOff;
        var switch_purifier = document.getElementById("onoffswitch_purifier");
        if (!switch_purifier.checked) {
            onOff = "1";
        }
        else {
            onOff = "0";
        }
        $.ajax({
            type: 'POST',
            url:'/igrsiot/control/purifier/control',
            data:{
                deviceId:"#lemx500s#78b3b912418f",
                lock:0,
                power:onOff,
                duration:30
            },
            // dataType:'json',
            contentType:'application/x-www-form-urlencoded; charset=utf-8',
            async:true,
            error: function(result) {
            },
            success: function(data) {
                var msg = "purifierSwitch" + ":" + onOff;
                webSocketsendMessage(msg);
            }
        });
    };

    $scope.sensorDataSwitch = function() {
        var date;
        var sensorType;
        var title;
        var dateObj = document.getElementById("sensorDate");
        date = dateObj.value;
        var objSig = document.getElementsByName("sensorTypeRadio");
        for (var i=0; i<objSig.length; i++) {
            if (objSig[i].checked) {
                sensorType = objSig[i].value;
            }
        }
        $.ajax({
            type: 'POST',
            url:'/igrsiot/control/sensor/history',
            data:{
                date:date,
                type:sensorType
            },
            contentType:'application/x-www-form-urlencoded; charset=utf-8',
            async:false,
            error: function(result) {
            },
            success: function(data) {
                console.log(data);
                var value = [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0];
                var buf;
                switch (sensorType) {
                    case 'pm25':
                        title = "PM2.5";
                        for (var i=0; i<data.length; i++) {
                            buf = data[i].value.split('.');
                            value[parseInt(data[i].hour, 10)] = buf[0];
                        }
                        break;
                    case 'co2':
                        title = "CO2";
                        for (var i=0; i<data.length; i++) {
                            buf = data[i].value.split('.');
                            value[parseInt(data[i].hour, 10)] = buf[0];
                        }
                        break;
                    case 'tvoc':
                        title = "TVOC";
                        for (var i=0; i<data.length; i++) {
                            buf = data[i].value.split('.');
                            var buff = data[i].value.substring(0, buf[0].length+4);
                            value[parseInt(data[i].hour, 10)] = buff;
                        }
                        break;
                    case 'temperature':
                        title = "温度";
                        for (var i=0; i<data.length; i++) {
                            buf = data[i].value.split('.');
                            var buff = data[i].value.substring(0, buf[0].length+2);
                            value[parseInt(data[i].hour, 10)] = buff;
                        }
                        break;
                    case 'humidity':
                        title = "湿度";
                        for (var i=0; i<data.length; i++) {
                            buf = data[i].value.split('.');
                            var buff = data[i].value.substring(0, buf[0].length+2);
                            value[parseInt(data[i].hour, 10)] = buff;
                        }
                        break;
                    case 'formaldehyde':
                        title = "甲醛";
                        for (var i=0; i<data.length; i++) {
                            buf = data[i].value.split('.');
                            var buff = data[i].value.substring(0, buf[0].length+4);
                            value[parseInt(data[i].hour, 10)] = buff;
                        }
                        break;
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

    // setInterval(function () {
    //     $.ajax({
    //         type: 'POST',
    //         url:'/igrsiot/control/status',
    //         contentType:'application/x-www-form-urlencoded; charset=utf-8',
    //         async:false,
    //         error: function(result) {
    //         },
    //         success: function(data) {
    //             console.log(data);
    //             var switchWelcome = document.getElementById("onoffswitch_welcomemode");
    //             var switchAll = document.getElementById("onoffswitch_all");
    //             var switchMachine1 = document.getElementById("onoffswitch_machine1");
    //             var machine1Sig = document.getElementsByName("machine1Radio");
    //             var machine1Volume = document.getElementById("machine1Volume");
    //             var switchMachine2 = document.getElementById("onoffswitch_machine2");
    //             var machine2Sig = document.getElementsByName("machine2Radio");
    //             var machine2Volume = document.getElementById("machine2Volume");
    //             var switchLed1 = document.getElementById("onoffswitch_led1");
    //             var switchLed2 = document.getElementById("onoffswitch_led2");
    //             var switchCurtain = document.getElementById("onoffswitch_curtain");
    //             var switchPurifier = document.getElementById("onoffswitch_purifier");
    //
    //             for (var i=0; i<data.length; i++) {
    //                 switch (data[i].deviceId) {
    //                     case "welcomemode":
    //                         switchWelcome.checked = data[i].value === "1";
    //                         break;
    //                     case "machine1":
    //                         switch (data[i].attribute) {
    //                             case "switch":
    //                                 switchMachine1.checked = data[i].value === "1";
    //                                 break;
    //                             case "sig_source":
    //                                 for (var j=0; j<machine1Sig.length; j++) {
    //                                     if (machine1Sig[j].value == data[i].value) {
    //                                         machine1Sig[j].checked = true;
    //                                         break;
    //                                     }
    //                                 }
    //                                 break;
    //                             case "volume":
    //                                 machine1Volume.value = data[i].value;
    //                                 break;
    //                             default:
    //                                 break;
    //                         }
    //                         break;
    //                     case "machine2":
    //                         switch (data[i].attribute) {
    //                             case "switch":
    //                                 switchMachine2.checked = data[i].value === "1";
    //                                 break;
    //                             case "sig_source":
    //                                 for (var j=0; j<machine2Sig.length; j++) {
    //                                     if (machine2Sig[j].value == data[i].value) {
    //                                         machine2Sig[j].checked = true;
    //                                         break;
    //                                     }
    //                                 }
    //                                 break;
    //                             case "volume":
    //                                 machine2Volume.value = data[i].value;
    //                                 break;
    //                             default:
    //                                 break;
    //                         }
    //                         break;
    //                     case "led1":
    //                         switchLed1.checked = data[i].value === "1";
    //                         break;
    //                     case "led2":
    //                         switchLed2.checked = data[i].value === "1";
    //                         break;
    //                     case "curtain":
    //                         switchCurtain.checked = data[i].value === "1";
    //                         break;
    //                     case "purifier":
    //                         switch (data[i].attribute) {
    //                             case "switch":
    //                                 switchPurifier.checked = data[i].value === "1";
    //                                 break;
    //                             case "lock":
    //                                 var check = document.getElementById("purifierLcCheck");
    //                                 check.checked = data[i].value === "1";
    //                                 break;
    //                             case "sleep":
    //                                 var check = document.getElementById("purifierSlCheck");
    //                                 check.checked = data[i].value === "1";
    //                                 break;
    //                             case "mode":
    //                                 var check = document.getElementById("purifierMoCheck");
    //                                 check.checked = data[i].value === "1";
    //                                 break;
    //                             case "anion":
    //                                 var check = document.getElementById("purifierIoCheck");
    //                                 check.checked = data[i].value === "1";
    //                                 break;
    //                             case "uv":
    //                                 var check = document.getElementById("purifierUvCheck");
    //                                 check.checked = data[i].value === "1";
    //                                 break;
    //                             case "timer":
    //                                 var check = document.getElementById("purifierTiCheck");
    //                                 check.checked = data[i].value === "1";
    //                                 break;
    //                             case "windspeed":
    //                                 var radio = document.getElementsByName("purifierRadio");
    //                                 for (var j=0; j<radio.length; j++) {
    //                                     if (radio[j].value == data[i].value) {
    //                                         radio[j].checked = true;
    //                                         break;
    //                                     }
    //                                 }
    //                                 break;
    //                             default:
    //                                 break;
    //                         }
    //                         break;
    //                     default:
    //                         break;
    //                 }
    //             }
    //             if (!switchMachine1.checked && !switchMachine2.checked && !switchLed1.checked && !switchLed2.checked && !switchCurtain.checked) {
    //                 switchAll.checked = false;
    //             }
    //             else {
    //                 switchAll.checked = true;
    //             }
    //         }
    //     });
    //
    //     $.ajax({
    //         type: 'POST',
    //         url:'/igrsiot/control/sensor',
    //         contentType:'application/x-www-form-urlencoded; charset=utf-8',
    //         async:true,
    //         error: function(result) {
    //         },
    //         success: function(data) {
    //             console.log(data);
    //             var result = data.split(",");
    //             var pm25 = result[0].split('.');
    //             DrawPm25('canvasDiv1', pm25[0]);
    //             DrawPm25('canvasDiv2', pm25[0]);
    //             DrawPm25('canvasDiv3', pm25[0]);
    //             DrawPm25('canvasDiv4', pm25[0]);
    //             DrawPm25('canvasDiv6', pm25[0]);
    //
    //             var co2Value = document.getElementsByName("co2Value");
    //             for (var i=0; i<co2Value.length; i++) {
    //                 var co2 = result[1].split('.');
    //                 co2Value[i].innerText = co2[0];
    //             }
    //
    //             var tvocValue = document.getElementsByName("tvocValue");
    //             for (var i=0; i<tvocValue.length; i++) {
    //                 tvocValue[i].innerText = result[2];
    //             }
    //
    //             var temperatureValue = document.getElementsByName("temperatureValue");
    //             for (var i=0; i<temperatureValue.length; i++) {
    //                 var temp = result[3].substring(0, result[3].length-2);
    //                 temperatureValue[i].innerText = temp;
    //             }
    //
    //             var humidityValue = document.getElementsByName("humidityValue");
    //             for (var i=0; i<humidityValue.length; i++) {
    //                 var hum = result[4].substring(0, result[4].length-2);
    //                 humidityValue[i].innerText = hum;
    //             }
    //
    //             var formaldehydeValue = document.getElementsByName("formaldehydeValue");
    //             for (var i=0; i<formaldehydeValue.length; i++) {
    //                 var co2 = result[1].split('.');
    //                 formaldehydeValue[i].innerText = result[5];
    //             }
    //
    //             if (data == 'SUCCESS') {
    //             } else {
    //             }
    //         }
    //     });
    // }, 30000);

    $(document).ready(function() {
        $.ajax({
            type: 'POST',
            url:'/igrsiot/control/status',
            contentType:'application/x-www-form-urlencoded; charset=utf-8',
            async:false,
            error: function(result) {
            },
            success: function(data) {
                console.log(data);
                var switchWelcome = document.getElementById("onoffswitch_welcomemode");
                var switchAll = document.getElementById("onoffswitch_all");
                var switchMachine1 = document.getElementById("onoffswitch_machine1");
                var machine1Sig = document.getElementsByName("machine1Radio");
                var machine1Volume = document.getElementById("machine1Volume");
                var switchMachine2 = document.getElementById("onoffswitch_machine2");
                var machine2Sig = document.getElementsByName("machine2Radio");
                var machine2Volume = document.getElementById("machine2Volume");
                var switchLed1 = document.getElementById("onoffswitch_led1");
                var switchLed2 = document.getElementById("onoffswitch_led2");
                var switchCurtain = document.getElementById("onoffswitch_curtain");
                var switchPurifier = document.getElementById("onoffswitch_purifier");

                for (var i=0; i<data.length; i++) {
                    switch (data[i].deviceId) {
                        case "welcomemode":
                            switchWelcome.checked = data[i].value === "1";
                            break;
                        case "machine1":
                            switch (data[i].attribute) {
                                case "switch":
                                    switchMachine1.checked = data[i].value === "1";
                                    break;
                                case "sig_source":
                                    for (var j=0; j<machine1Sig.length; j++) {
                                        if (machine1Sig[j].value == data[i].value) {
                                            machine1Sig[j].checked = true;
                                            break;
                                        }
                                    }
                                    break;
                                case "volume":
                                    machine1Volume.value = data[i].value;
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case "machine2":
                            switch (data[i].attribute) {
                                case "switch":
                                    switchMachine2.checked = data[i].value === "1";
                                    break;
                                case "sig_source":
                                    for (var j=0; j<machine2Sig.length; j++) {
                                        if (machine2Sig[j].value == data[i].value) {
                                            machine2Sig[j].checked = true;
                                            break;
                                        }
                                    }
                                    break;
                                case "volume":
                                    machine2Volume.value = data[i].value;
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case "led1":
                            switchLed1.checked = data[i].value === "1";
                            break;
                        case "led2":
                            switchLed2.checked = data[i].value === "1";
                            break;
                        case "curtain":
                            switchCurtain.checked = data[i].value === "1";
                            break;
                        case "purifier":
                            switch (data[i].attribute) {
                                case "switch":
                                    switchPurifier.checked = data[i].value === "1";
                                    break;
                                case "lock":
                                    var check = document.getElementById("purifierLcCheck");
                                    check.checked = data[i].value === "1";
                                    break;
                                case "sleep":
                                    var check = document.getElementById("purifierSlCheck");
                                    check.checked = data[i].value === "1";
                                    break;
                                case "mode":
                                    var check = document.getElementById("purifierMoCheck");
                                    check.checked = data[i].value === "1";
                                    break;
                                case "anion":
                                    var check = document.getElementById("purifierIoCheck");
                                    check.checked = data[i].value === "1";
                                    break;
                                case "uv":
                                    var check = document.getElementById("purifierUvCheck");
                                    check.checked = data[i].value === "1";
                                    break;
                                case "timer":
                                    var check = document.getElementById("purifierTiCheck");
                                    check.checked = data[i].value === "1";
                                    break;
                                case "windspeed":
                                    var radio = document.getElementsByName("purifierRadio");
                                    for (var j=0; j<radio.length; j++) {
                                        if (radio[j].value == data[i].value) {
                                            radio[j].checked = true;
                                            break;
                                        }
                                    }
                                    break;
                                default:
                                    break;
                            }
                            break;
                        default:
                            break;
                    }
                }
                if (!switchMachine1.checked && !switchMachine2.checked && !switchLed1.checked && !switchLed2.checked && !switchCurtain.checked) {
                    switchAll.checked = false;
                }
                else {
                    switchAll.checked = true;
                }
            }
        });

        $.ajax({
            type: 'POST',
            url:'/igrsiot/control/sensor',
            contentType:'application/x-www-form-urlencoded; charset=utf-8',
            async:true,
            error: function(result) {
            },
            success: function(data) {
                console.log(data);
                var result = data.split(",");
                var pm25 = result[0].split('.');
                DrawPm25('canvasDiv1', pm25[0]);
                DrawPm25('canvasDiv2', pm25[0]);
                DrawPm25('canvasDiv3', pm25[0]);
                DrawPm25('canvasDiv4', pm25[0]);
                DrawPm25('canvasDiv6', pm25[0]);

                var co2Value = document.getElementsByName("co2Value");
                for (var i=0; i<co2Value.length; i++) {
                    var co2 = result[1].split('.');
                    co2Value[i].innerText = co2[0];
                }

                var tvocValue = document.getElementsByName("tvocValue");
                for (var i=0; i<tvocValue.length; i++) {
                    tvocValue[i].innerText = parseFloat(result[2]).toFixed(3);
                }

                var temperatureValue = document.getElementsByName("temperatureValue");
                for (var i=0; i<temperatureValue.length; i++) {
                    var temp = parseFloat(result[3]).toFixed(1);
                    temperatureValue[i].innerText = temp;
                }

                var humidityValue = document.getElementsByName("humidityValue");
                for (var i=0; i<humidityValue.length; i++) {
                    var hum = parseFloat(result[4]).toFixed(1);
                    humidityValue[i].innerText = hum;
                }

                var formaldehydeValue = document.getElementsByName("formaldehydeValue");
                for (var i=0; i<formaldehydeValue.length; i++) {
                    var co2 = result[1].split('.');
                    formaldehydeValue[i].innerText = parseFloat(result[5]).toFixed(3);
                }

                if (data == 'SUCCESS') {
                } else {
                }
            }
        });

        var objSig = document.getElementsByName("sensorTypeRadio");
        objSig[0].checked = true;
        $.ajax({
            type: 'POST',
            url:'/igrsiot/control/sensor/history',
            data:{
                date:"",
                type:"pm25"
            },
            contentType:'application/x-www-form-urlencoded; charset=utf-8',
            async:false,
            error: function(result) {
            },
            success: function(data) {
                console.log(data);
                var value = [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0];
                var buf;
                title = "PM2.5";
                for (var i=0; i<data.length; i++) {
                    buf = data[i].value.split('.');
                    value[parseInt(data[i].hour, 10)] = buf[0];
                }
                console.log(value);
                DrawSensor('canvasDiv5', title, value);
            }
        });

        $.ajax({
            type: 'POST',
            url:'/igrsiot/control/operate',
            data: {
                pageNo:curPageNo
            },
            contentType:'application/x-www-form-urlencoded; charset=utf-8',
            async:false,
            error: function(result) {
            },
            success: function(data) {
                console.log(data);
                if (data.length != 0) {
                    totalPage = data[0].totalPage;
                    var paginationTextObj = document.getElementById("paginationText");
                    paginationTextObj.innerText = curPageNo + '/' + totalPage;
                    $scope.data = data;
                }
            }
        });
    });

    $scope.prevPage = function () {
        if (curPageNo == 1) {
            return;
        }
        curPageNo --;

        $.ajax({
            type: 'POST',
            url:'/igrsiot/control/operate',
            data: {
                pageNo:curPageNo
            },
            contentType:'application/x-www-form-urlencoded; charset=utf-8',
            async:false,
            error: function(result) {
            },
            success: function(data) {
                console.log(data);
                totalPage = data[0].totalPage;
                var paginationTextObj = document.getElementById("paginationText");
                paginationTextObj.innerText = curPageNo + '/' + totalPage;
                $scope.data = data;
            }
        });
    }

    $scope.nextPage = function () {
        if (curPageNo == totalPage) {
            return;
        }
        curPageNo ++;

        $.ajax({
            type: 'POST',
            url:'/igrsiot/control/operate',
            data: {
                pageNo:curPageNo
            },
            contentType:'application/x-www-form-urlencoded; charset=utf-8',
            async:false,
            error: function(result) {
            },
            success: function(data) {
                console.log(data);
                totalPage = data[0].totalPage;
                var paginationTextObj = document.getElementById("paginationText");
                paginationTextObj.innerText = curPageNo + '/' + totalPage;
                $scope.data = data;
            }
        });
    }
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
    var Width = 280;
    var Height = 200;
    var Fontsize = 14;
    var screenwidth = document.body.clientWidth
    // console.log(screenwidth);
    // if (screenwidth >= 970) {
    //     Width = 300;
    //     Height = 300;
    //     Fontsize = 20;
    // }
    // else if (screenwidth<970 && screenwidth>750) {
    //     Width = 280;
    //     Height = 200;
    //     Fontsize = 12;
    // }
    // else {
    //     Width = 280;
    //     Height = 200;
    //     Fontsize = 10;
    // }

    var text = 'PM 2.5\n' + value + '(μg/m³)';

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
        offsetx:40,
        shadow:true,
        background_color:'white',
        separate_angle:0,//分离角度
        border:0,
        tip:{
            enable:true,
            showType:'fixed'
        },
        legend : {
            enable:false,
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
    var Width = 370;
    var Height = 240;
    var Fontsize = 14;
    var screenwidth = document.body.clientWidth
    if (screenwidth >= 970) {
        Width = 500;
        Height = 300;
        Fontsize = 20;
    }
    else if (screenwidth<970 && screenwidth>750) {
        Fontsize = 16;
    }
    else {
        Width = 300;
        Height = 200;
        Fontsize = 12;
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