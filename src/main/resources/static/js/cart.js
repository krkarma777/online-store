
function submitFormWithCartId() {
    var subtotal = parseFloat($("#subtotal").text().replace('원', '').replace(',', ''));

    // 합계가 0원인지 확인합니다.
    if (subtotal === 0) {
        // 사용자에게 메시지를 표시하고 함수를 종료합니다.
        alert('선택하신 상품이 없습니다.');
        return;
    }

    var cartId = document.getElementById('cartId').value;
    var form = document.querySelector('form');

    var previouslyAddedInputs = form.querySelectorAll("input[name='itemId']");
    previouslyAddedInputs.forEach(input => input.remove());

    document.querySelectorAll("input[name='itemId']:checked").forEach(function(checkbox) {
        var input = document.createElement("input");
        input.type = "hidden";
        input.name = "itemId";
        input.value = checkbox.value;
        form.appendChild(input);
    });

    form.action = '/order/' + cartId;
    form.submit();
}
// 아이템의 가격, 할인, 배송비를 업데이트하는 함수
function updateCartSummary() {
    var subtotal = 0;
    var discount = 0;
    var shipping = 0;

    // 체크된 아이템들의 가격을 합산합니다.
    $("input[name='itemId']:checked").each(function () {
        var itemId = $(this).val();
        var itemTotal = parseFloat($('#item-' + itemId).find(".item-total").text().replace('원', '').replace(',', ''));
        subtotal += itemTotal;
    });

    // 할인금액과 배송비를 설정합니다.
    if (subtotal >= 150000) {
        discount = 5990;
    }

    // 배송비 계산 로직 (여기서는 무료 배송을 가정합니다)
    shipping = 0;

    var total = subtotal - discount + shipping;

    $("#subtotal").text(subtotal);
    $("#discount").text(discount);
    $("#shipping").text(shipping);
    $("#total").text(total);
}


$(document).ready(function () {
    // 전체 선택/해제 체크박스 이벤트
    $("#checkAll").click(function () {
        $("input[type='checkbox']").prop('checked', $(this).prop('checked'));
        updateCartSummary(); // 체크박스 상태 변경 시 장바구니 요약 업데이트
    });

    // 개별 체크박스 상태 변경 이벤트
    $("input[name='itemId']").change(function() {
        updateCartSummary(); // 체크박스 상태 변경 시 장바구니 요약 업데이트
    });

    $('.delete-btn').on('click', function () {
        var itemId = $(this).data('itemid'); // 'data-itemid' 속성에서 itemId를 가져옵니다.
        deleteCartItem(itemId);
    });

    // 수량 조정 버튼 클릭 이벤트
    $('.quantity-change').click(function () {
        var itemId = $(this).data('itemid');
        var operation = $(this).data('operation');
        var input = $("input[name='quantity-" + itemId + "']");
        var currentQuantity = parseInt(input.val());

        if (operation === "increase") {
            currentQuantity += 1;
        } else if (operation === "decrease" && currentQuantity > 1) {
            currentQuantity -= 1;
        }

        input.val(currentQuantity);
        updateCartItemQuantity(itemId, currentQuantity);
        updateCartSummary();
    });

    // 수량 입력 변경 이벤트
    $(document).on('change', '.quantity-input', function () {
        var itemId = $(this).data('itemid');
        var quantity = $(this).val();
        // If the field is empty, set the quantity to 1
        if (quantity === '') {
            quantity = '1';
            $(this).val(quantity);
        }
        updateCartItemQuantity(itemId, quantity);
    });
    updateCartSummary();
});


function deleteCartItem(itemId) {
    $.ajax({
        url: '/cart/delete',
        type: 'POST',
        data: {itemId: itemId},
        success: function (response) {
            // 삭제 성공 시 페이지에서 해당 항목 제거
            // 예: $('#item-' + itemId).remove();
            alert('상품이 삭제되었습니다.');
            $('#item-' + itemId).remove();
            updateCartSummary();
        },
        error: function (xhr, status, error) {
            // 에러 처리, 서버로부터의 응답을 바탕으로 메시지를 표시
            var errorMessage = xhr.responseJSON ? xhr.responseJSON.message : '삭제 중 오류가 발생했습니다.';
            alert('삭제 중 오류가 발생했습니다.');
        }
    });
}

function deleteSelectedItems() {
    var selectedItems = [];
    $("input[name='itemId']:checked").each(function () {
        selectedItems.push($(this).val());
    });

    $.ajax({
        url: '/cart/deleteSelected',
        type: 'POST',
        traditional: true,
        data: {itemIds: selectedItems},
        success: function (response) {
            // 성공 처리
            alert('선택된 상품이 삭제되었습니다.');
            location.reload(); // 페이지 새로고침
            updateCartSummary();
        },
        error: function (error) {
            // 실패 처리
            alert('삭제 중 오류가 발생했습니다.');
        }
    });
}

function updateCartItemQuantity(itemId, quantity) {
    $.ajax({
        url: '/cart/update',
        type: 'POST',
        data: {
            itemId: itemId,
            quantity: quantity
        },
        success: function (response) {
            var price = parseFloat($("tr[id='item-" + itemId + "']").find("td:eq(2)").text().replace('원', '').replace(',', '')); // 가격을 텍스트로 가져오는 대신에 숫자로 파싱하여 계산
            var newTotal = price * quantity;
            $("tr[id='item-" + itemId + "']").find(".item-total").text(newTotal + '원'); // 총 가격을 계산하여 텍스트로 설정
            updateCartSummary();
        },
        error: function (error) {
            alert('수량 업데이트 중 오류가 발생했습니다.');
        }
    });



// 페이지가 준비된 후 초기 한 번만 장바구니 요약을 업데이트합니다.
    $(document).ready(function () {
        updateCartSummary();
    });

    $(document).on('input', '.quantity-input', function() {
        this.value = this.value.replace(/\D/g, '');
    });
    $('.quantity-input').on('change', function() {
        updateCartItemQuantity($(this).data('itemid'), $(this).val());
    });
    updateCartSummary();
}

