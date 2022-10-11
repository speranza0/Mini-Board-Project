$(document).ready(function () {
  // 헤더 스크롤 처리
  if ($(window).scrollTop() >= 64) {
    $("#header").addClass("sticky");
  }

  // 헤더 스크롤 이벤트
  $(window).scroll(function () {
    var scrollTop = $(this).scrollTop();
    if (scrollTop >= 64) {
      $("#header").addClass("sticky");
    } else {
      $("#header").removeClass("sticky");
    }
  });
});
