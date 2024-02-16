
    document.addEventListener('DOMContentLoaded', function () {
    // 모든 드롭다운 토글 버튼에 대한 이벤트 리스너 추가
    var dropdowns = document.querySelectorAll('.dropdown-toggle');
    dropdowns.forEach(function(dropdown) {
    dropdown.addEventListener('click', function(event) {
    // 부모 메뉴의 토글 동작 방지
    event.stopPropagation();
    // 현재 클릭된 메뉴의 드롭다운만 토글
    var parentDropdownMenu = dropdown.nextElementSibling;
    if (parentDropdownMenu.classList.contains('show')) {
    parentDropdownMenu.classList.remove('show');
} else {
    // 이미 열려 있는 모든 드롭다운 메뉴를 닫음
    var openMenus = document.querySelectorAll('.dropdown-menu.show');
    openMenus.forEach(function(openMenu) {
    openMenu.classList.remove('show');
});
    // 현재 메뉴를 열음
    parentDropdownMenu.classList.add('show');
}
});
});

    // 바깥쪽 클릭 시 드롭다운 메뉴 닫기
    document.body.addEventListener('click', function(e) {
    var openMenus = document.querySelectorAll('.dropdown-menu.show');
    openMenus.forEach(function(menu) {
    if (!menu.contains(e.target)) {
    menu.classList.remove('show');
}
});
});
});
