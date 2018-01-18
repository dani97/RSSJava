/**
 * 
 */
var document = window.document;
getNews("news?content=SPORTS");
var topNav = document.getElementsByClassName("top-nav")[0];
topNav.addEventListener('click',function perform(event){
    performTask(event);
});
function getNews(url){
	var news = document.getElementById("rss-section");
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			news.innerHTML = this.responseText;
		}
	};
	xhttp.open("GET",url, true);
	xhttp.send();
}

function performTask(event){
	var src = event.srcElement;
	getNews(src.dataset.id);
}