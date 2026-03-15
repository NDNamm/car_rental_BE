logimg=false;
		$('#img1').load(function() { 
		//alert("image loaded correctly");
		})
      $('#img1').error(function() { 
	  //alert("error loading image"); 
	  url1=$('#img1').attr("src");
	  newurl=url1.replace("www1.bonbanh.com","www.bonbanh.com");
	  $('#img1').attr("src",newurl);
	  $(".list_thumb img").each(
		function(intIndex) {
			surl1=$(this).attr("src");
			newsurl=surl1.replace("www1.bonbanh.com","www.bonbanh.com");
			$(this).attr("src",newsurl);
		}
		);
		if(!logimg) 
		{
			$("body").append("...");
			logimg=true;
		}
	  
	  })