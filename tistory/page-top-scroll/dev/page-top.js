const toTopEl = document.querySelector("#page-top");

/**
 * 쓰로틀링 미적용
 */
// window.addEventListener("scroll", function() { 
//   console.log("SCROLL!!");
//   if (window.scrollY > 800) {
//     toTopEl.style.transform = "translateX(0)";
//     return;
//   }
//   // 버튼 숨기기
//   toTopEl.style.transform = "translateX(100px)";
// });

/**
 * lodash 쓰로틀링 적용
 */
window.addEventListener("scroll", _.throttle(function() { 
  console.log("SCROLL!!");
  if (window.scrollY > 500) {
    toTopEl.style.transform = "translateX(0)";
    return;
  }
  // 버튼 숨기기
  toTopEl.style.transform = "translateX(100px)";
}, 300));

toTopEl.addEventListener("click", function() {
  window.scrollTo({
    top: 0,
    behavior: "smooth"
  });
});