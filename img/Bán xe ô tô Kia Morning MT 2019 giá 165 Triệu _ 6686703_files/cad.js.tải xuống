var bid=0;
var time_click=0;
function click_ad(id)
{	
	//alert(id);
	var _d = new Date();
	_t=_d.getTime(); 
	if((_t-time_click)<15000)
	{
		//alert("double click");
	}else
	{
		bid=id	;	
		setTimeout('log_click_ad()',300);
	}
	time_click=_t;
	
	//log_click_ad();
}
function log_click_ad()
{		
	
	if(bid>0)
	{
		id=bid;
		bid=0;
		var _ct = new Date();
$.get("cad.php?bid="+id+"&vid=e5daaa90c369adfd156862d6df632ded"+'&t='+_ct.getTime(),{}, function(data){
				if(data.length >0) {				
					//do nothing
					//alert(data);					
				}
			});
			
	}	
}

