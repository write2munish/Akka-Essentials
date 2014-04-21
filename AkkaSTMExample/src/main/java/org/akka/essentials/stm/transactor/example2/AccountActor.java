package org.akka.essentials.stm.transactor.example2;

import org.akka.essentials.stm.transactor.example2.msg.AccountBalance;
import org.akka.essentials.stm.transactor.example2.msg.AccountCredit;
import org.akka.essentials.stm.transactor.example2.msg.AccountDebit;

import scala.concurrent.stm.Ref;
import scala.concurrent.stm.japi.STM;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.transactor.UntypedTransactor;

public class AccountActor extends UntypedTransactor {

	LoggingAdapter log = Logging.getLogger(getContext().system(), this);

	String accountNumber;
	// Use the scala STM Ref for state variables that need to
	// participate in transactions
	Ref.View<Float> balance = STM.newRef(Float.parseFloat("0"));

	public AccountActor(String accNo, Float bal) {
		this.accountNumber = accNo;
		balance.set(bal);
	}

	// default method to be overridden
	@Override
	public void atomically(Object message) throws Exception {
		if (message instanceof AccountDebit) {
			AccountDebit accDebit = (AccountDebit) message;
			// check for funds availability
			if (balance.get() > accDebit.getAmount()) {
				float bal = balance.get() - accDebit.getAmount();
				balance.set(bal);
			} else {
				throw new IllegalStateException("Insufficient Balance");
			}

		} else if (message instanceof AccountCredit) {

			AccountCredit accCredit = (AccountCredit) message;
			float bal = balance.get() + accCredit.getAmount();
			balance.set(bal);

		}
	}

	// To completely bypass coordinated transactions override the normally
	// method.
	@Override
	public boolean normally(Object message) {
		if (message instanceof AccountBalance) {
			// reply with the account balance
			sender().tell(new AccountBalance(accountNumber, balance.get()));
			return true;
		}
		return false;
	}
}
