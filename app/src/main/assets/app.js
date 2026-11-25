let apps=[];let drawerOpen=false;
const $=id=>document.getElementById(id);
function tick(){const d=new Date();$('time').textContent=d.toLocaleTimeString([], {hour:'2-digit',minute:'2-digit',hour12:true}).replace(' ','');$('date').textContent=d.toLocaleDateString([], {weekday:'long',month:'short',day:'numeric'});$('greeting').textContent=d.getHours()<12?'Good morning. Make today yours.':d.getHours()<18?'Good afternoon. Keep things simple.':'Good evening. Take a moment to unwind.'}
function loadApps(){try{apps=window.J2OS?JSON.parse(J2OS.getApps()):[]}catch(e){apps=[]}renderHome();renderApps()}
function iconMarkup(a){return a.icon?`<img src="${a.icon}" alt="">`:`<div class="placeholder"></div>`}
function appButton(a){return `<button class="app" onclick="launch('${a.packageName.replace(/'/g,"")}')">${iconMarkup(a)}<span>${escapeHtml(a.name)}</span></button>`}
function renderHome(){const preferred=apps.filter(a=>/phone|camera|message|settings|chrome/i.test(a.name)).slice(0,8);$('quickApps').innerHTML=preferred.slice(0,4).map(appButton).join('');$('dock').innerHTML=preferred.slice(4,8).map(appButton).join('')}
function renderApps(){const q=($('search').value||'').toLowerCase();const list=apps.filter(a=>a.name.toLowerCase().includes(q));$('appGrid').innerHTML=list.length?list.map(appButton).join(''):'<div class="empty">No matching apps</div>';$('appCount').textContent=apps.length+' apps'}
function launch(pkg){if(window.J2OS)J2OS.launchApp(pkg)}
function openDrawer(){drawerOpen=true;$('drawer').classList.add('open');$('search').value='';renderApps()}
function closeDrawer(){drawerOpen=false;$('drawer').classList.remove('open')}
function escapeHtml(s){return String(s).replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]))}
tick();setInterval(tick,1000);loadApps();
let startY=0;document.addEventListener('touchstart',e=>{startY=e.touches[0].clientY},{passive:true});document.addEventListener('touchend',e=>{const delta=startY-e.changedTouches[0].clientY;if(delta>65&&startY>window.innerHeight-100)openDrawer();if(delta< -65&&drawerOpen)closeDrawer()},{passive:true});
