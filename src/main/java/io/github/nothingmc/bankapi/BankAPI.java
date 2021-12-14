package io.github.nothingmc.bankapi;

import com.google.common.util.concurrent.ListenableFuture;
import io.github.nothingmc.account.Account;
import io.github.nothingmc.account.AccountantGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import javax.annotation.Nonnull;

public class BankAPI {
    private final @Nonnull AccountantGrpc.AccountantFutureStub asyncStub;

    /**
     * Can handle exception with {@link io.grpc.StatusRuntimeException} (when using get())
     * @param host BankAPI gRPC host
     * @param port BankAPI gRPC host port
     */
    public BankAPI(@Nonnull String host, int port) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();

        asyncStub = AccountantGrpc.newFutureStub(channel);
    }

    public ListenableFuture<Account.OpenResponse> createAccount(@Nonnull String accountId) {
        return asyncStub.open(
                Account.OpenRequest
                        .newBuilder()
                        .setAccountId(accountId)
                        .build()
        );
    }

    public ListenableFuture<Account.GetBalanceResponse> getBalance(@Nonnull String accountId) {
        return asyncStub.getBalance(
                Account.GetBalanceRequest
                        .newBuilder()
                        .setAccountId(accountId)
                        .build()
        );
    }

    public ListenableFuture<Account.DepositResponse> deposit(@Nonnull String accountId, long amount) {
        return deposit(accountId, (double) amount);
    }

    public ListenableFuture<Account.DepositResponse> deposit(@Nonnull String accountId, double amount) {
        return asyncStub.deposit(
                Account.DepositRequest
                        .newBuilder()
                        .setAccountId(accountId)
                        .setAmount(amount)
                        .build()
        );
    }

    public ListenableFuture<Account.WithdrawResponse> withdraw(@Nonnull String accountId, long amount) {
        return withdraw(accountId, (double) amount);
    }

    public ListenableFuture<Account.WithdrawResponse> withdraw(@Nonnull String accountId, double amount) {
        return asyncStub.withdraw(
                Account.WithdrawRequest
                        .newBuilder()
                        .setAccountId(accountId)
                        .setAmount(amount)
                        .build()
        );
    }

    public ListenableFuture<Account.SendResponse> send(@Nonnull String fromAccountId, @Nonnull String toAccountId, long amount) {
        return send(fromAccountId, toAccountId, (double) amount);
    }

    public ListenableFuture<Account.SendResponse> send(@Nonnull String fromAccountId, @Nonnull String toAccountId, double amount) {
        return asyncStub.send(
                Account.SendRequest
                        .newBuilder()
                        .setFromAccountId(fromAccountId)
                        .setToAccountId(toAccountId)
                        .setAmount(amount)
                        .build()
        );
    }
}
