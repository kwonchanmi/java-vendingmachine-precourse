package vendingmachine.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import vendingmachine.domain.Product;
import vendingmachine.domain.Products;

public class Validation {

	public static void isHoldingAmount(String input) {
		int holdingAmount;

		try {
			holdingAmount = Integer.parseInt(input);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(ErrorMessage.COMMON + ErrorMessage.HOLDING_AMOUNT_NUM);
		}

		if (holdingAmount < Constant.HOLDING_AMOUNT_MIN) {
			throw new IllegalArgumentException(ErrorMessage.COMMON + ErrorMessage.HOLDING_AMOUNT_MIN);
		}

		if (holdingAmount > Constant.HOLDING_AMOUNT_MAX) {
			throw new IllegalArgumentException(ErrorMessage.COMMON + ErrorMessage.HOLDING_AMOUNT_MAX);
		}

		if (holdingAmount % Constant.HOLDING_AMOUNT_DIVIDE != Constant.HOLDING_AMOUNT_REMAINDER) {
			throw new IllegalArgumentException(ErrorMessage.COMMON + ErrorMessage.HOLDING_AMOUNT_DIVIDE);
		}
	}

	public static void isProducts(String input) {
		if (input.endsWith(Constant.PRODUCTS_SPLIT)) {
			throw new IllegalArgumentException(ErrorMessage.COMMON + ErrorMessage.PRODUCTS_SPLIT);
		}

		String[] products = input.split(Constant.PRODUCTS_SPLIT);
		long count = Arrays.stream(products)
			.filter(e -> !e.startsWith(Constant.PRODUCT_START) || !e.endsWith(Constant.PRODUCT_LAST))
			.count();
		if (count != 0) {
			throw new IllegalArgumentException(ErrorMessage.COMMON + ErrorMessage.PRODUCT_SPLIT);
		}

		List<String[]> product = Arrays.stream(products)
			.map(e -> e.substring(1, e.length() - 1))
			.map(e -> e.split(Constant.PRODUCT_SPLIT))
			.collect(Collectors.toList());
		count = product.stream()
			.filter(e -> e.length != Constant.PRODUCT_NUM)
			.count();
		if (count != 0) {
			throw new IllegalArgumentException(ErrorMessage.COMMON + ErrorMessage.PRODUCT_INPUT);
		}

		Set<String> productName = product.stream()
			.map(e -> e[Constant.PRODUCT_NAME_INDEX])
			.collect(Collectors.toSet());
		if (productName.size() != product.size()) {
			throw new IllegalArgumentException(ErrorMessage.COMMON + ErrorMessage.PRODUCT_NAME_DUPLICATE);
		}
	}

	public static void isEnteredAmount(String input) {
		int enteredAmount;
		try {
			enteredAmount = Integer.parseInt(input);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(ErrorMessage.COMMON + ErrorMessage.ENTERED_AMOUNT_NUM);
		}
		if (enteredAmount < Constant.ENTERED_AMOUNT_MIN) {
			throw new IllegalArgumentException(ErrorMessage.COMMON + ErrorMessage.ENTERED_AMOUNT_MIN);
		}
		if (enteredAmount > Constant.ENTERED_AMOUNT_MAX) {
			throw new IllegalArgumentException(ErrorMessage.COMMON + ErrorMessage.ENTERED_AMOUNT_MAX);
		}
		if (enteredAmount % Constant.ENTERED_AMOUNT_DIVIDE != Constant.ENTERED_AMOUNT_REMAINDER) {
			throw new IllegalArgumentException(ErrorMessage.COMMON + ErrorMessage.ENTERED_AMOUNT_DIVIDE);
		}
	}

	public static void isProductToBuy(Products products, String input, int enteredAmount) {
		Optional<Product> product = products.findByName(input);
		if (!product.isPresent()) {
			throw new IllegalArgumentException(ErrorMessage.COMMON + ErrorMessage.PRODUCT_TO_BUY_EMPTY_PRODUCT);
		}
		if (product.get().isEmpty()) {
			throw new IllegalArgumentException(ErrorMessage.COMMON + ErrorMessage.PRODUCT_TO_BUY_EMPTY_COUNT);
		}
		if (!product.get().isBuy(enteredAmount)) {
			throw new IllegalArgumentException(ErrorMessage.COMMON + ErrorMessage.PRODUCT_TO_BUY_LACK);
		}
	}

}
