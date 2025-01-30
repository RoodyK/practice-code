const pageTopEl = document.querySelector("#page-top");

window.addEventListener("scroll", _.throttle(function() { 
  // 버튼 나타나기
  if (window.scrollY > 500) {
    pageTopEl.style.transform = "translateX(0)";
    return;
  }
  // 버튼 숨기기
  pageTopEl.style.transform = "translateX(100px)";
}, 300));

// top 버튼 클릭시 최상단 이동
pageTopEl.addEventListener("click", function() {
  window.scrollTo({
    top: 0,
    behavior: "smooth"
  });
});