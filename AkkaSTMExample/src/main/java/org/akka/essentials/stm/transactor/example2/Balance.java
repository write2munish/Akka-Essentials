package org.akka.essentials.stm.transactor.example2;

import scala.concurrent.stm.Ref;
import scala.concurrent.stm.japi.STM;
import scala.concurrent.stm.japi.STM.Transformer;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.transactor.UntypedTransactor;

public class Balance extends UntypedTransactor {
	LoggingAdapter log = Logging.getLogger(getContext().system(), this);

	Ref.View<Float> balance = STM.newRef(Float.parseFloat("0"));

	//default method to be overridden 
	@Override
	public void atomically(Object message) throws Exception {
		if (message instanceof AccountDebit) {
			final AccountDebit accDebit = (AccountDebit) message;
			// check for funds availability
			if (balance.get() < accDebit.getAmount()) {
				throw new IllegalStateException("Insufficient Balance");
			} else {
				STM.getAndTransform(balance, new Transformer<Float>() {
					@Override
					public Float apply(Float arg0) {
						Float bal = balance.get() - accDebit.getAmount();
						return bal;
					}
				});
			}
		} else if (message instanceof AccountCredit) {
			final AccountCredit accCredit = (AccountCredit) message;
			STM.getAndTransform(balance, new Transformer<Float>() {
				@Override
				public Float apply(Float arg0) {
					Float bal = balance.get() + accCredit.getAmount();
					return bal;
				}
			});
		}
	}

	// To completely bypass coordinated transactions override the normally
	// method.
	@Override
	public boolean normally(Object message) {
		if (message instanceof String) {
			if ("BALANCE".equals(message)) {
				getSender().tell(balance.get());
				return true;
			} else
				return false;
		} else
			return false;
	}
}
