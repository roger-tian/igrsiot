angular.module('starter.services', [])
//service factory provider controllers
//service 构造器 对象 方法
//factory 直接对函数进行构造 如果定义一个函数后必须进行return
//定义的响应变量需要在控制器里面注入  可以重用
//provider 在service 里面注入,去使用该服务器的功能
//controller 控制器 模块之间的控制方法  不能重用

.factory('lister',function(){
	var list=[
		{
 	"img":"img/c1ali.png",
 	"title":"新媒体运营",
 	"money":"6千-8千",
 	"gongsi":"廊坊市麦芽信息有限公司",
 	"data":"今天",
 	"city":"廊坊",
 	"xueli":"学历不限"
 },
 {
 	"img":"img/c1iqiyi.jpg",
 	"title":"H5开发工程师",
 	"money":"4千-6千",
 	"gongsi":"石家庄创软网络科技有限……",
 	"data":"今天",
 	"city":"北京",
 	"xueli":"大专"
 },
 {
 	"img":"img/c1ele.png",
 	"title":"国企招WEB/HTML5……",
 	"money":"6千-8千",
 	"gongsi":"中青才智教育投资",
 	"data":"今天",
 	"city":"北京",
 	"xueli":"大专"
 },
 {
 	"img":"img/c1jigndong.jpg",
 	"title":"前端开发工程师",
 	"money":"8千-1万",
 	"gongsi":"北京金木堂文化发展有限……",
 	"data":"今天",
 	"city":"北京",
 	"xueli":"本科"
 },
 {
 	"img":"img/sec.ctrip.com.jpg",
 	"title":"前端研发工程师",
 	"money":"1万-2万",
 	"gongsi":"宏华国际医疗控股有限……",
 	"data":"今天",
 	"city":"北京",
 	"xueli":"大专"
 },
 {
 	"img":"img/www.yangcong.com.jpg",
 	"title":"前端开发工程师",
 	"money":"8千-1.6万",
 	"gongsi":"北京汉雅天诚教育科技……",
 	"data":"昨天",
 	"city":"北京",
 	"xueli":"本科"
 },
 {
 	"img":"img/www.tass.com.cn.jpg",
 	"title":"H5开发",
 	"money":"6千-1.2万",
 	"gongsi":"北京艾优薇文化科技……",
 	"data":"今天",
 	"city":"北京-朝阳",
 	"xueli":"大专"
 },
 {
 	"img":"img/sec.ctrip.com.jpg",
 	"title":"诚聘前端工程师",
 	"money":"8千-1万",
 	"gongsi":"蓝豹迅捷(北京)科技……",
 	"data":"03-06",
 	"city":"北京",
 	"xueli":"大专"
 },
 {
 	"img":"img/httpbobao.360.cn.jpg",
 	"title":"前端开发",
 	"money":"6千-8千",
 	"gongsi":"北京博习园教育有限……",
 	"data":"今天",
 	"city":"北京",
 	"xueli":"本科"
 }
	]
	return{
		all:function(){
			return list
		}
	}
})

