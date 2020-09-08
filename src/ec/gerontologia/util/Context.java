package ec.gerontologia.util;

import ec.gerontologia.modelo.Centro;
import ec.gerontologia.modelo.Usuario;

public class Context {
	private final static Context instance = new Context();
	public static Context getInstance() {
		return instance;
	}
	private Usuario usuarioLogeado;
	private Centro centro;

	public Usuario getUsuarioLogeado() {
		return usuarioLogeado;
	}

	public void setUsuarioLogeado(Usuario usuarioLogeado) {
		this.usuarioLogeado = usuarioLogeado;
	}

	public Centro getCentro() {
		return centro;
	}

	public void setCentro(Centro centro) {
		this.centro = centro;
	}
	
}
