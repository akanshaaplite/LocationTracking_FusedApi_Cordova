function success(){
}
function failure(){
}
var locResultPlugin =  {
	mainMethodForAll:function(data) {
	if(data!="")
		return cordova.exec(success,failure,"locResultPlugin","mainMethodForAll",[data]);
		else
		 alert("Please provide time interval to track loaction");
		console.log(data)
	},
	stop:function() {
		return cordova.exec(success,failure,"locResultPlugin","stop",[""]);
	},
	locationCheck:function(){
	return cordova.exec(success,failure,"locResultPlugin","locationCheck",[""]);
	}
};
