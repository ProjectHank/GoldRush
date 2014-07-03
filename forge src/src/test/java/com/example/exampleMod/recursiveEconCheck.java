// This is java pseudo code to show the planned recursive economics modeling function 
/*
main {
	temp = price;
	price = Math.Roof(sellStone(25));
	}

int SellStone(n) {

	if (n =1) {
		temp = temp - (temp/100);
    		return temp;
	}
	if (n > 1) {
   		temp = temp - (temp/100);
    		return temp + SellStone(n-1);

	}
}

//again psuedocode, no implementation whatsoever
@test
public void test() {
	sell(gold, 5)
	org.junit.Assert.assertTrue("Econ error", x.getValue(gold) = calcValue(gold,5));
}

int calcValue(MCitem item, int sellNum){
	while (sellNum > 0){
		item.value = item.value - 0.01 * item.value;
		sellNum--;
	}
} 
