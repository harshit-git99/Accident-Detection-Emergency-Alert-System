let lat = null;
let lon = null;

const $ = (id) => document.getElementById(id);
const statusEl = $("status");

async function fetchStatus(){
  try{
    const r = await fetch('/api/status', {cache:'no-store'});
    statusEl.textContent = JSON.stringify(await r.json(), null, 2);
  }catch(e){
    statusEl.textContent = 'Status error: ' + e;
  }
}

async function triggerAccident(){
  const severity = $("severity").value;
  const message = $("message").value;

  const payload = { severity, message };
  if(lat !== null && lon !== null){
    payload.latitude = lat;
    payload.longitude = lon;
  }

  const btn = $("btnTrigger");
  btn.disabled = true;
  statusEl.textContent = 'Triggering...';

  try{
    const r = await fetch('/api/trigger-accident',{
      method:'POST',
      headers:{'Content-Type':'application/json'},
      body: JSON.stringify(payload)
    });
    const data = await r.json();
    statusEl.textContent = JSON.stringify(data, null, 2);

    // refresh after a bit (SMS send is async)
    setTimeout(fetchStatus, 2500);
  }catch(e){
    statusEl.textContent = 'Trigger error: ' + e;
  }finally{
    btn.disabled = false;
  }
}

async function attachLocation(){
  const btn = $("btnLocation");
  btn.disabled = true;
  try{
    if(!navigator.geolocation){
      alert('Geolocation not supported on this browser/device.');
      return;
    }
    navigator.geolocation.getCurrentPosition((pos)=>{
      lat = pos.coords.latitude;
      lon = pos.coords.longitude;
      $("lat").textContent = lat;
      $("lon").textContent = lon;
      btn.disabled = false;
    }, (err)=>{
      alert('Location error: ' + err.message);
      btn.disabled = false;
    }, { enableHighAccuracy:true, timeout: 10000 });
  }catch(e){
    alert(e);
    btn.disabled = false;
  }
}

$("btnTrigger").addEventListener('click', triggerAccident);
$("btnLocation").addEventListener('click', attachLocation);

fetchStatus();
setInterval(fetchStatus, 5000);

