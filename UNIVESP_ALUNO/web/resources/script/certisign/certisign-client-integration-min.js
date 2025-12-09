CertisignIntegration=function(e,f,d,a,b,c){	                       
	                   this._onSignCompleted=[];
	                   this.Url=e;
	                   this.Width=f;
	                   this.Height=d;
	                   this.CloseOnFinish=a;
	                   this.DisableCloseInProcessing=c;
	                   this.ConfirmCloseConfig=b
	                   };
	                   
	                   var isArray=false,
	                   isPostObject=false;
	                   (function(a){
	                	    var g;
	                	         var h=function(l){
	                	    	       return/^(?:\w+\:\/\/[^\/]+)/.exec(l)[0]
	                	    	     };
	                	    	 var d=function(m){
		                	    		 for(var l=0;l<m.length;l++){
		                	    			 m[l]=+m[l]
		                	    		 }
	                	    		     return m
	                	    		  };
	                	    	 var c=function(){
	                	    		    modalSign.parentElement.removeChild(modalSign)
	                	    		   };
	                	         var k=function(m){
	                	        	               var l=document.querySelector("#close");
				                	        	    if(l){
														  if(!m){
															      if(g.DisableCloseInProcessing!=false){
															    	  (l.removeEventListener)?l.removeEventListener("click",c):l.detachEvent("click",c)
															    			  
															      }if(g.DisableCloseInProcessing){
															    		l.className="close-disabled";l.href="";return
															      }else{
																    	   if(g.DisableCloseInProcessing===undefined||g.DisableCloseInProcessing==null){												    						   
																    			 l.className="close";
																    			 l.href="#popover"
																    		}
															    	   }
					    			                           }else{
					    			                        	   (l.addEventListener)?l.addEventListener("click",c):l.attachEvent("click",c)
					    			                        	    }
														}
	                	        	           };
	                	          var e=function(){
	                	        	            /*   var p=document.createElement("div");
	                	        	               var q=document.createElement("div");
	                	        	               var n=document.createElement("div");
	                	        	               var o=document.createElement("div");
	                	        	               var m=document.createElement("a");
	                	        	               var l=document.createElement("a");
	                	        	               p.id="popover";
	                	        	               p.className="row";
	                	        	               q.className="popovers";
	                	        	               q.style.position="absolute";
	                	        	               q.style.top=0;
	                	        	               q.style.margin="25px";
	                	        	               q.style.marginTop="370px";
	                	        	               q.style.borderRadius=".3rem";
	                	        	               q.style.border="1px solid rgba(0, 0, 0, .2)";
	                	        	               n.className="content";
	                	        	               o.className="footer";
	                	        	               m.className="btn btn-primary";
	                	        	               l.className="btn btn-default";
	                	        	               n.innerText=g.ConfirmCloseConfig&&g.ConfirmCloseConfig.textMessage?g.ConfirmCloseConfig.textMessage:"Closing will stop the signing process. Confirm?";
	                	        	               m.innerText=g.ConfirmCloseConfig&&g.ConfirmCloseConfig.textOkButton?g.ConfirmCloseConfig.textOkButton:"Ok";
	                	        	               l.innerText=g.ConfirmCloseConfig&&g.ConfirmCloseConfig.textCancelButton?g.ConfirmCloseConfig.textCancelButton:"Cancel";
	                	        	               m.href="#";
	                	        	               l.href="#";
	                	        	               p.appendChild(q);
	                	        	               q.appendChild(n);
	                	        	               o.appendChild(m);
	                	        	               o.appendChild(l);
	                	        	               q.appendChild(o);
	                	        	               modalSign.querySelector(".modal-body").appendChild(p);
	                	        	               (m.addEventListener)?m.addEventListener("click",c):m.attachEvent("click",c)*/
	                	        	            };
	                	       var b=function(m){
	                	    	                  var l=document.createElement("iframe");
	                	    	                      l.id="signFrame";
	                	    	                      l.src+=m+"&autosign=true";
	                	    	                      l.width=0;
	                	    	                      l.height=0;
	                	    	                      l.allow="geolocation";
	                	    	                      l.style.borderWidth=0;
	                	    	                      document.body.appendChild(l)
	                	    	                 };
	                	        var f=function(r,s,l){
	                	        	                  s=s?s:innerWidth-30;l=l?l:innerHeight-200;
	                	        	                  modalSign=document.createElement("div");
	                	        	                  var n=document.createElement("div");
	                	        	                  var o=document.createElement("div");
	                	        	                  var p=document.createElement("a");
	                	        	                  var m=document.createElement("div");
	                	        	                  var q=document.createElement("iframe");
	                	        	                  q.id="signFrame";
	                	        	                  q.src=r;
	                	        	                  q.width="100%";
	                	        	                  q.minHeight="auto"; 
	                	        	                  q.height="523px";
	                	        	                  q.style.borderWidth=0;
	                	        	                  q.allow="geolocation; camera";
	                	        	                  n.style.width="90%";
	                	        	                  n.style.height="auto";
	                	        	                  modalSign.id="modalSign";
	                	        	                  p.id="close";
	                	        	                  modalSign.className="modal";
	                	        	                  n.className="modal-content";
	                	        	                  o.className="modal-headers";
	                	        	                  m.className="modal-body";
	                	        	                  p.className="closes";
	                	        	                  p.innerHTML="&times;";
	                	        	                  o.appendChild(p);
	                	        	                  n.appendChild(o);
	                	        	                  n.appendChild(m);
	                	        	                  modalSign.appendChild(n);
	                	        	                  m.appendChild(q);
	                	        	                  modalSign.style.display="block";
	                	        	                  modalSign.style.zIndex=999;
	                	        	                  modalSign.style.paddingTop="80px";
	                	        	                  modalSign.style.paddingLeft="160px";
	                	        	                  
	                	        	                  o.style.textAlign="end";
	                	        	                  o.style.alignItems="flex-start";
	                	        	                  o.style.justifyContent="space-between";
	                	        	                  o.style.padding="1rem 1rem";
	                	        	                  o.style.borderBottom="1px solid #e3e6f0";
	                	        	                  o.style.borderTopLeftRadius=".3rem";
	                	        	                  o.style.borderTopRightRadius=".3rem";
	                	        	                  o.style.cursor="pointer";
	                	        	                 
	                	        	                  
	                	        	                  document.body.appendChild(modalSign);
	                	        	                  (p.addEventListener)?p.addEventListener("click",c):p.attachEvent("click",c);
	                	        	                  e();
	                	        	                return q
	                	        	                };
	                	        	a.prototype={
	                	        			    closeModal:function(){
	                	        			    	                  c()
	                	        			    	                 }
	                	        	                                 ,onSignCompleted:function(l){
	                	        			    	                	                         this._onSignCompleted=l
	                	        			    	                	                         }
	                	        			    	                 ,sign:function(){
	                	        			    	                	              g=this;
	                	        			    	                	              if(arguments[0]==="auto"){
	                	        			    	                	            	                        b(g.Url)
	                	        			    	                	            	                        }else{
	                	        			    	                	            	                        	   var m=f(g.Url,g.Width,g.Height);
	                	        			    	                	            	                        	    if(arguments.length==2){
	                	        			    	                	            	                        	    	var l=arguments[0];
	                	        			    	                	            	                        	    	var n=arguments[1];
	                	        			    	                	            	                        	    	   m.onload=function(){
	                	        			    	                	            	                        	    		                    isArray=(Array.isArray(l)&&(l=l.join(","))&&true);
	                	        			    	                	            	                        	    		                    var o=h(g.Url);
	                	        			    	                	            	                        	    		                    isPostObject?m.contentWindow.postMessage({certisignMessage:"Sign",docsToSign:l,signer:n},o):m.contentWindow.postMessage("certisignMessage:Sign;docsToSign:"+l+";signer:"+n,o)
	                	        			    	                	            	                        	    		                  }
	                	        			    	                	            	                        	    	                  }
	                	        			    	                	            	                        	 }
	                	        			    	                	                                             return g
	                	        			    	                	            	                        	 
	                	        			    	                                 }
	                	                        };
	                	        	var j=function(m){
	                	        		              isPostObject=(typeof(m.data)!="string");
	                	        		              if(/.portaldeassinaturas.com.br*|localhost/.test(m.origin)){
	                	        		            	  var n={};
	                	        		            	  if(isPostObject){
	                	        		            		                 n=m.data;console.log("objReceived:",n);
	                	        		            		                 if(m.data.statusObj!=undefined||m.data.statusObj!=null){
	                	        		            		            	     n.signatureStatusList=m.data.statusObj.statusList?m.data.statusObj.statusList:m.data.statusObj.code;console.log(n)
	                	        		            		                 }
	                	        		            		              }else{
	                	        		            		            	      var l=m.data.split(";");
	                	        		            		            	      var o;
	                	        		            		            	      for(i=0;i<l.length;o=l[i].split(":"),n[o[0]]=o[1],i++){	                	        		            		            		    
	                	        		            		            	      }
	                	        		            		            	    }
	                	        		            	                       if(n.certisignMessage==="SignatureStatus"){
	                	        		            	                    	       k(true);
	                	        		            	                    	       var p=isArray?(String(n.signatureStatusList).indexOf(",")!=-1?d(n.signatureStatusList.split(",")):d([n.signatureStatusList])):n.signatureStatusList;console.log("signStatus",p);
	                	        		            	                    	       g._onSignCompleted(p,m.data.statusObj);
	                	        		            	                    	       g.CloseOnFinish&&c()
	                	        		            	                        }else{
	                	        		            	                        	  if(n.certisignMessage==="SignStarted"){
	                	        		            	                        		k(!(n.value==="true"))
	                	        		            	                        	  }
	                	        		            	                        }
	                	        		               }
	                	        		             };
	                	        		          (window.addEventListener)?window.addEventListener("message",j):window.attachEvent("onmessage",j);
	                	        		          ("postMessage" in window)&&window.postMessage({},"*")
	                	     }
	                   )
	                   (CertisignIntegration);