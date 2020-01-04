var URLEncoder = Java.type("java.net.URLEncoder");
model["encoded"] = function(){
	return function(s){
		return URLEncoder.encode(Mustache.render(s,model),"UTF-8");
	};
};