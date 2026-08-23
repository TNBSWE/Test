import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class AccountTest {

    @Test
    void testGetMaxOverdrawn() {
        Account myTestAccount = new Account(BigDecimal.ZERO, "SEK", BigDecimal.ZERO);
        assertEquals(BigDecimal.ZERO, myTestAccount.getMaxOverdrawn());

        Account myTestAccount2 = new Account(BigDecimal.ZERO, "SEK", new BigDecimal(-1));
        assertEquals(BigDecimal.ZERO, myTestAccount2.getMaxOverdrawn()); //max_overdrawn must be non-negative

        Account myTestAccount3 = new Account(BigDecimal.ZERO, "SEK", new BigDecimal(1000));
        assertEquals(new BigDecimal(1000), myTestAccount3.getMaxOverdrawn());
    }
    @Test
    void testSetMaxOverdrawn() {
        Account myTestAccount = new Account(BigDecimal.ZERO, "SEK", BigDecimal.ZERO);
        myTestAccount.setMaxOverdrawn(new BigDecimal(-1));
        assertEquals(BigDecimal.ZERO, myTestAccount.getMaxOverdrawn()); //max_overdrawn must be non-negative

        myTestAccount.setMaxOverdrawn(new BigDecimal(100));
        assertEquals(new BigDecimal(100), myTestAccount.getMaxOverdrawn()); //max_overdrawn must be non-negative
    }
    @Test
    void testGetCurrency() {
        Account myTestAccount = new Account(BigDecimal.ZERO,  "SEK", BigDecimal.ZERO);
        assertEquals("SEK", myTestAccount.getCurrency());

        myTestAccount = new Account(BigDecimal.ZERO,  "EUR", BigDecimal.ZERO);
        assertEquals("EUR", myTestAccount.getCurrency());

        myTestAccount = new Account(BigDecimal.ZERO,  "USD", BigDecimal.ZERO);
        assertEquals("USD", myTestAccount.getCurrency());
    }
    @Test
    void testSetCurrency() {
        Account myTestAccount = new Account(BigDecimal.ZERO, "SEK", BigDecimal.ZERO);
        myTestAccount.setCurrency("EUR");
        assertEquals("EUR", myTestAccount.getCurrency());
        myTestAccount.setCurrency("SEK");
        assertEquals("SEK", myTestAccount.getCurrency());
    }
    @Test
    void testGetBalance() {
        Account myTestAccount = new Account(BigDecimal.ZERO, "SEK", BigDecimal.ZERO);
        assertEquals(BigDecimal.ZERO, myTestAccount.getBalance());
        myTestAccount = new Account(new BigDecimal(100), "SEK", BigDecimal.ZERO);
        assertEquals(new BigDecimal(100), myTestAccount.getBalance());
    }
    @Test
    void testSetBalance() {
        Account myTestAccount = new Account(BigDecimal.ZERO, "SEK", BigDecimal.ONE);
        //should not be allowed to set balance to lower that -1 * maxOverdrawn
        myTestAccount.setBalance(new BigDecimal(-2));
        assertEquals(BigDecimal.ZERO, myTestAccount.getBalance());
        myTestAccount.setBalance(new BigDecimal(42));
        assertEquals(new BigDecimal(42), myTestAccount.getBalance());
    }

    @Test
        void testWithdraw() {
        // Test case #1
        // System Expected behavior: when 100 is withdrawn from 351.00, there should be left 251.00 and the balance ...
        // should be updated.
        // The provided code skeleton behavior: Computed balance - amount but never assigned the result back to this.balance (BigDecimal is immutable)...
        // so getBalance() still returned 351.00.
        // Why input values: Positive balance withdrawal, easy to check.
        // Fixing the code : Assign back the result of subtraction, this.balance = this.balance.subtract(amount);.

        Account myTestAccount = new Account(new BigDecimal("351.00"), "SEK", BigDecimal.ZERO);
        BigDecimal newBalance = myTestAccount.withdraw(new BigDecimal("100.00"));
        assertEquals(new BigDecimal("251.00"), newBalance);
        assertEquals(new BigDecimal("251.00"), myTestAccount.getBalance()); // balance must be updated.
        // Test case #2
        // System Expected behavior:When the entire balance is drawn, the system should return 0.00 and zeroes balance.
        // The provided code skeleton behavior: Same mutation bug — balance was never updated.
        // Why input values: Boundary - withdraw exact available funds.
        // Fixing the code : no.
        Account myTestAccount2 = new Account(new BigDecimal("500.00"), "SEK", BigDecimal.ZERO);
        BigDecimal newBalance1 = myTestAccount2.withdraw(new BigDecimal("500.00"));
        assertEquals(new BigDecimal("0.00"), newBalance1);
        assertEquals(new BigDecimal("0.00"), myTestAccount2.getBalance()); // balance must be updated.
        // Test case #3
        // System Expected behavior:A withdrawal that would push balance below -max_overdrawn must be rejected;...
        // balance stays unchanged.
        // The provided code skeleton behavior: No overdraft guard — the method would return -300 even though...
        // max_overdrawn is 200, meaning the minimum allowed balance is -200.
        // Why input values: Partition — invalid withdrawal that violates the overdraft limit.
        // Fixing the code : Added guard: if (newBalance < -max_overdrawn) return this. balance unchanged.
        Account myTestAccount3 = new Account(new BigDecimal("100.00"), "SEK",  new BigDecimal("200.00"));
        BigDecimal newBalance2 = myTestAccount3.withdraw(new BigDecimal("400.00")); // -300 < -200 => reject
        assertEquals(new BigDecimal("100.00"), newBalance2);
        assertEquals(new BigDecimal("100.00"), myTestAccount3.getBalance()); // balance must be updated.
    }

    //@Test
    //void testDeposit() {

        // Test case #1
        // System Expected behavior: when 200 is deposited to 100.00, there should be total 300.00 in the account ...
        // and the balance should be updated.
        // The provided code skeleton behavior: Computed balance - amount but never assigned the result back to ...
        // this.balance (BigDecimal is immutable), so getBalance() still returned 200.00.
        // Why input values: Positive value is deposited, easy to check.
        // Fixing the code : Assign back the result of addition, this.balance = this.balance.add(amount);.
      //  Account myTestAccount = new Account(new BigDecimal("100.00"), "SEK",  BigDecimal.ZERO);
        //BigDecimal newBalance = myTestAccount.deposit(new BigDecimal("200.00"));
        //assertEquals(new BigDecimal("300.00"), newBalance);
        //assertEquals(new BigDecimal("300.00"), myTestAccount.getBalance()); // balance must be updated.
        // Test case #2
        // System Expected behavior: when 0 is deposited to 100.00, there should be total 100.00 in the account ...
        // and the balance should be updated.
        // The provided code skeleton behavior: Computed balance - amount but never assigned the result back to ...
        // this.balance (BigDecimal is immutable), so getBalance() still returned 200.00.
        // Why input values: Positive value is deposited, easy to check.
        // Fixing the code : Assign back the result of addition, this.balance = this.balance.add(amount);.
        //Account myTestAccount1 = new Account(new BigDecimal("100.00"), "SEK",  BigDecimal.ZERO);
        //BigDecimal newBalance1 = myTestAccount.deposit(new BigDecimal("200.00"));
        //assertEquals(new BigDecimal("300.00"), newBalance);
        //assertEquals(new BigDecimal("300.00"), myTestAccount.getBalance()); // balance must be updated.
    //}

    //@Test
    //void testConvertToCurrency() {
      //  fail("Not yet implemented"); //TODO implement
    // }

    // @Test
    // void testTransferToAccount() {
    //  fail("Not yet implemented"); //TODO implement
    //}

    //@Test
    //void testWithdrawAll() {
    //  fail("Not yet implemented"); //TODO implement
    //}
}
