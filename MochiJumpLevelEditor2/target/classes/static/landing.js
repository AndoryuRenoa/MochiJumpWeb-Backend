counter = 0;
	document.getElementById("option1").addEventListener("mouseenter", opt1Show);
	document.getElementById("option1").addEventListener("mouseleave", opt1Hide);
	document.getElementById("option2").addEventListener("mouseenter", opt2Show);
	document.getElementById("option2").addEventListener("mouseleave", opt2Hide);
	document.getElementById("option2").addEventListener("click", showAbout);
	document.getElementById("hideAbout").addEventListener("click",hideAbout);
	document.getElementById("option3").addEventListener("mouseenter", opt3Show);
	document.getElementById("option3").addEventListener("mouseleave", opt3Hide);
	id = setInterval (optAni, 150);
	function optAni(){
	
			if (counter == 5){
				clearInterval(id);
			}
			else if (counter == 0){
				document.getElementById("selectorimg1").src ="/images/bone1M.png";
				document.getElementById("selectorimg2").src ="/images/bone1M.png";
				document.getElementById("selectorimg3").src ="/images/bone1M.png";
				counter++;
			}
			else if (counter == 1){
				document.getElementById("selectorimg1").src ="/images/bone2M.png";
				document.getElementById("selectorimg2").src ="/images/bone2M.png";
				document.getElementById("selectorimg3").src ="/images/bone2M.png";
				counter++;
			}else if (counter == 2){
				document.getElementById("selectorimg1").src ="/images/bone3M.png";
				document.getElementById("selectorimg2").src ="/images/bone3M.png";
				document.getElementById("selectorimg3").src ="/images/bone3M.png";
				counter++;
			}else if (counter == 3){
				document.getElementById("selectorimg1").src ="/images/bone4M.png";
				document.getElementById("selectorimg2").src ="/images/bone4M.png";
				document.getElementById("selectorimg3").src ="/images/bone4M.png";
				counter =0;
			}
		}
	function opt1Show(){
	document.getElementById("selectorimg1").style.visibility="visible";
	}	
	function opt1Hide(){
	document.getElementById("selectorimg1").style.visibility="hidden";	
	}
	function opt2Show(){
	document.getElementById("selectorimg2").style.visibility="visible";
	}	
	function opt2Hide(){
	document.getElementById("selectorimg2").style.visibility="hidden";	
	}
	function showAbout(){
		document.getElementById("modal").style.visibility="visible";
	}
	function hideAbout(){
		document.getElementById("modal").style.visibility="hidden";
	}
	function opt3Show(){
		document.getElementById("selectorimg3").style.visibility="visible";
		}	
	function opt3Hide(){
		document.getElementById("selectorimg3").style.visibility="hidden";	
	}