/*
 * This file is part of the Lupus Client distribution (https://github.com/MeteorDevelopment/lupus-client/).
 * Copyright (c) 2021 Lupus Development.
 */

package coloursplash.lupusclient.accounts.types;

import com.google.gson.Gson;
import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import coloursplash.lupusclient.LupusClient;
import coloursplash.lupusclient.accounts.Account;
import coloursplash.lupusclient.accounts.AccountType;
import coloursplash.lupusclient.mixin.MinecraftClientAccessor;
import coloursplash.lupusclient.utils.misc.NbtException;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Session;
import net.minecraft.nbt.CompoundTag;

public class PremiumAccount extends Account<PremiumAccount> {
    private static final Gson GSON = new Gson();

    private String password;

    public PremiumAccount(String name, String password) {
        super(AccountType.Premium, name);
        this.password = password;
    }

    @Override
    public boolean fetchInfo() {
        YggdrasilUserAuthentication auth = getAuth();

        try {
            auth.logIn();

            cache.username = auth.getSelectedProfile().getName();
            cache.uuid = auth.getSelectedProfile().getId().toString();

            return true;
        } catch (AuthenticationException e) {
            return false;
        }
    }

    @Override
    public boolean fetchHead() {
        try {
            return cache.makeHead("https://crafatar.com/avatars/" + cache.uuid + "?size=8&overlay&default=MHF_Steve");
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean login() {
        super.login();

        YggdrasilUserAuthentication auth = getAuth();

        try {
            auth.logIn();
            setSession(new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), "mojang"));

            cache.username = auth.getSelectedProfile().getName();
            return true;
        } catch (AuthenticationUnavailableException e) {
            LupusClient.LOG.error("Failed to contact the authentication server.");
            return false;
        } catch (AuthenticationException e) {
            if (e.getMessage().contains("Invalid username or password") || e.getMessage().contains("account migrated"))
                LupusClient.LOG.error("Wrong password.");
            else LupusClient.LOG.error("Failed to contact the authentication server.");
            return false;
        }
    }

    public YggdrasilUserAuthentication getAuth() {
        YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication) new YggdrasilAuthenticationService(((MinecraftClientAccessor) MinecraftClient.getInstance()).getProxy(), "").createUserAuthentication(Agent.MINECRAFT);

        auth.setUsername(name);
        auth.setPassword(password);

        return auth;
    }

    @Override
    public CompoundTag toTag() {
        CompoundTag tag = super.toTag();

        tag.putString("password", password);

        return tag;
    }

    @Override
    public PremiumAccount fromTag(CompoundTag tag) {
        super.fromTag(tag);
        if (!tag.contains("password")) throw new NbtException();

        password = tag.getString("password");

        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PremiumAccount)) return false;
        return ((PremiumAccount) o).name.equals(this.name);
    }
}