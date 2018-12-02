package br.com.campeonatinho.utils;

import org.jcommon.encryption.SimpleMD5;

import br.com.campeonatinho.entity.Usuario;

public class Encrypt {

	public static void encryptPassword(Usuario usuario) {

		if (usuario != null) {
			if (usuario.getSenha() != null) {
				SimpleMD5 md5 = new SimpleMD5(usuario.getSenha(), "aquiestoumaisumdiasoboolharsanguinariodovigia");
				usuario.setSenha(md5.toHexString());
			}
		}
	}

	public static String geraChaveAtivacao(String email) {

		SimpleMD5 md5 = new SimpleMD5(email, "aerialsintheskywhenyoulosesmallmindyoufreeyourlife");
		return md5.toHexString();
	}
}
