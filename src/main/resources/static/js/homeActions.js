function addToCart() {
    var productName = $('#productName').val();
    var price = $('#price').val();
    var img = $('#img').val();

    var formData = {
        productName: productName,
        price: price,
        img: img
    };

    // Send AJAX request to addToCart endpoint
    $.ajax({
        type: 'POST',
        url: $('#addToCartForm').attr('action'),
        data: formData,
        success: function(response) {
            alert(response); // Show a success message
        },
        error: function(xhr, status, error) {
            console.error(xhr.responseText);
            alert('Failed to add item to cart');
        }
    });
}