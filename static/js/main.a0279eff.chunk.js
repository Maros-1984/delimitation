(this["webpackJsonpdelimitation-frontend"]=this["webpackJsonpdelimitation-frontend"]||[]).push([[0],[,,,,,,,,,function(e,t,n){},function(e,t,n){},function(e,t,n){},,function(e,t,n){},function(e,t,n){"use strict";n.r(t);var o=n(1),r=n.n(o),i=n(4),a=n.n(i),s=(n(9),n(2)),l=(n(10),"https://delimitation.ey.r.appspot.com/"),c=function(){var e=window;return{width:e.innerWidth,height:e.innerHeight}},d=(n(11),n(0)),h=function(){return Object(d.jsx)("div",{className:"loader"})},u=function(e){var t=e.gameWidth,n=e.color,r=e.areaX,i=e.areaY,a=e.setHighlightedMove,l=e.moveToMake,u=.95/t*function(){var e=Object(o.useState)(c()),t=Object(s.a)(e,2),n=t[0],r=t[1];return Object(o.useEffect)((function(){function e(){r(c())}return window.addEventListener("resize",e),function(){return window.removeEventListener("resize",e)}}),[]),n}().width,j={paddingBottom:u+"px",width:u+"px"};return Object(d.jsx)("td",{onMouseMove:function(e){var t=e.nativeEvent.offsetX,n=e.nativeEvent.offsetY;a(t>n?u-t>n?{areaX:r,areaY:i-1,bottom:!0}:{areaX:r,areaY:i,right:!0}:u-t>n?{areaX:r-1,areaY:i,right:!0}:{areaX:r,areaY:i,bottom:!0})},className:n.toLowerCase()+"Area",style:j,children:l&&l.areaX===r&&l.areaY===i&&Object(d.jsx)(h,{})})},j=function(e){var t,n,o=e.cellIndex,r=e.gameWidth,i=e.rowIndex,a=e.game,s=e.highlightedMove,l={paddingBottom:95/r+"%",width:5/(r+1)+"%"},c=(null===s||void 0===s?void 0:s.areaX)===o&&(null===s||void 0===s?void 0:s.areaY)===i&&(null===s||void 0===s?void 0:s.right),h=null===(t=a.moves.find((function(e){return e.areaX===o&&e.areaY===i&&e.right})))||void 0===t||null===(n=t.color)||void 0===n?void 0:n.toLowerCase();return Object(d.jsx)(d.Fragment,{children:o!==r-1&&Object(d.jsx)("td",{style:l,className:h||(function(e,t){return a.possibleMoves.find((function(n){return n.areaX===t&&n.areaY===e&&n.right}))}(i,o)?c?"highlightedBorder":"possibleMove border":"border")})})},g=function(e){var t,n,o=e.rowIndex,r=e.cellIndex,i=e.gameWidth,a=e.game,s=e.highlightedMove,l={paddingBottom:5/(i+1)+"%",width:95/i+"%"},c=(null===s||void 0===s?void 0:s.areaX)===r&&(null===s||void 0===s?void 0:s.areaY)===o&&(null===s||void 0===s?void 0:s.bottom),h=null===(t=a.moves.find((function(e){return e.areaX===r&&e.areaY===o&&e.bottom})))||void 0===t||null===(n=t.color)||void 0===n?void 0:n.toLowerCase();return Object(d.jsx)("td",{style:l,className:h||(function(e,t){return a.possibleMoves.find((function(n){return n.areaX===t&&n.areaY===e&&n.bottom}))}(o,r)?c?"highlightedBorder":"possibleMove border":"border")})},b=function(e){var t=e.cellIndex,n=e.gameWidth,o=5/(n+1)+"%",r={paddingBottom:o,width:o};return Object(d.jsx)(d.Fragment,{children:t!==n-1&&Object(d.jsx)("td",{style:r,className:"border"})})},m=(n(13),function(e){var t=e.game;if(!t)return null;if(t.over){var n=t.score.BLUE,o=t.score.RED;return Object(d.jsxs)("div",{className:"infoMessage",children:[Object(d.jsx)("h1",{children:"Game Over"}),n>o&&Object(d.jsxs)("h2",{children:[Object(d.jsx)("span",{style:{color:"blue"},children:"BLUE"})," player won ",Object(d.jsx)("span",{style:{color:"blue"},children:n})," tiles to ",Object(d.jsx)("span",{style:{color:"red"},children:"RED"}),"'s ",Object(d.jsx)("span",{style:{color:"red"},children:o})," tiles."]}),n<o&&Object(d.jsxs)("h2",{children:[Object(d.jsx)("span",{style:{color:"red"},children:"RED"})," player won ",Object(d.jsx)("span",{style:{color:"red"},children:o})," tiles to ",Object(d.jsx)("span",{style:{color:"blue"},children:"BLUE"}),"'s ",Object(d.jsx)("span",{style:{color:"blue"},children:n})," tiles."]}),n===o&&Object(d.jsxs)("h2",{children:["It's a draw. ",Object(d.jsx)("span",{style:{color:"blue"},children:"BLUE"})," player has ",Object(d.jsx)("span",{style:{color:"blueviolet"},children:n})," tiles as well as ",Object(d.jsx)("span",{style:{color:"red"},children:"RED"})," player."]})]})}return 0===t.possibleMoves.length?Object(d.jsxs)("div",{className:"infoMessage",children:[Object(d.jsxs)("h1",{className:"glow",children:["Wait for the ",Object(d.jsx)("span",{style:{color:t.playerOnMove.toLowerCase()},children:"other player"})," to move."]}),t.moves.length<=1&&Object(d.jsx)("p",{children:"Send him the URL if you have not already."})]}):0===t.moves.length?Object(d.jsx)("div",{className:"infoMessage",children:Object(d.jsx)("h1",{children:"Make your first move on the corners."})}):null}),v=function(){var e=Object(o.useState)(),t=Object(s.a)(e,2),n=t[0],r=t[1],i=Object(o.useState)(),a=Object(s.a)(i,2),c=a[0],v=a[1],f=function(){var e=Object(o.useState)(),t=Object(s.a)(e,2),n=t[0],r=t[1];return Object(o.useEffect)((function(){var e,t=null===(e=document.location.hash)||void 0===e?void 0:e.substring(1),n=sessionStorage.getItem("playerColor");t!==sessionStorage.getItem("playerColor")&&(n=null),(null===t||void 0===t?void 0:t.length)>0?fetch(l+"get-game-status",{method:"POST",body:JSON.stringify({gameId:t,playerAsking:n}),headers:{"Content-Type":"application/json"}}).then((function(e){return e.json()})).then((function(e){r(e),sessionStorage.setItem("playerColor",e.yourPlayerColor),sessionStorage.setItem("gameId",e.gameId)})):fetch(l+"create-new-game",{method:"POST",body:"{}",headers:{"Content-Type":"application/json"}}).then((function(e){return e.json()})).then((function(e){r(e),document.location.hash=e.gameId,sessionStorage.setItem("playerColor",e.yourPlayerColor),sessionStorage.setItem("gameId",e.gameId)}))}),[]),{game:n,setGame:r}}(),O=f.game,p=f.setGame;if(function(e,t,n,r){Object(o.useEffect)((function(){e&&fetch(l+"make-move",{method:"POST",body:JSON.stringify({gameId:n.gameId,move:e,player:sessionStorage.getItem("playerColor")}),headers:{"Content-Type":"application/json"}}).then((function(e){return e.json()})).then((function(e){t(null),r(e)}))}),[null===n||void 0===n?void 0:n.gameId,null===n||void 0===n?void 0:n.playerOnMove,e,r,t])}(c,v,O,p),function(e,t){Object(o.useEffect)((function(){!e||e.possibleMoves.length>0||setTimeout((function(){return fetch(l+"get-game-status",{method:"POST",body:JSON.stringify({gameId:e.gameId,playerAsking:sessionStorage.getItem("playerColor"),incremental:!0}),headers:{"Content-Type":"application/json"}}).then((function(e){return e.json()})).then((function(e){return t(e)}))}),2e3)}),[e,t])}(O,p),!O)return Object(d.jsx)(h,{});var x=O.areas[0].length;return Object(d.jsxs)("div",{onClick:function(){O.possibleMoves.find((function(e){return e.areaX===(null===n||void 0===n?void 0:n.areaX)&&e.areaY===(null===n||void 0===n?void 0:n.areaY)&&(e.right&&(null===n||void 0===n?void 0:n.right)||e.bottom&&(null===n||void 0===n?void 0:n.bottom))}))&&v(n)},children:["\xa0",Object(d.jsx)("table",{children:Object(d.jsx)("tbody",{children:O.areas.map((function(e,t){return Object(d.jsxs)(o.Fragment,{children:[Object(d.jsx)("tr",{children:e.map((function(e,i){return Object(d.jsxs)(o.Fragment,{children:[Object(d.jsx)(u,{gameWidth:x,color:e,areaY:t,areaX:i,setHighlightedMove:r,moveToMake:c}),Object(d.jsx)(j,{cellIndex:i,gameWidth:x,rowIndex:t,game:O,highlightedMove:n})]},"cell-"+t+"-"+i)}))}),t!==O.areas.length-1&&Object(d.jsx)("tr",{children:e.map((function(e,r){return Object(d.jsxs)(o.Fragment,{children:[Object(d.jsx)(g,{rowIndex:t,cellIndex:r,gameWidth:x,highlightedMove:n,game:O}),Object(d.jsx)(b,{cellIndex:r,gameWidth:x})]},"bottom-border-of-cell-"+t+"-"+r)}))},"bottom-borders-of-row-"+t)]},"row-"+t)}))})}),Object(d.jsx)(m,{game:O})]})},f=function(){return Object(d.jsx)(v,{})},O=function(e){e&&e instanceof Function&&n.e(3).then(n.bind(null,15)).then((function(t){var n=t.getCLS,o=t.getFID,r=t.getFCP,i=t.getLCP,a=t.getTTFB;n(e),o(e),r(e),i(e),a(e)}))};a.a.render(Object(d.jsx)(r.a.StrictMode,{children:Object(d.jsx)(f,{})}),document.getElementById("root")),O()}],[[14,1,2]]]);
//# sourceMappingURL=main.a0279eff.chunk.js.map