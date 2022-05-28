$('.guide_all_images').slice(0,6).show();

$('#btnMore').on('click', function() {
  $('.guide_all_images:hidden').slice(0,3).slideDown();
  if($('.guide_all_images:hidden').length === 0) {
    $('#btnMore').fadeOut();
  }
});
