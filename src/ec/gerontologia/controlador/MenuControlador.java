package ec.gerontologia.controlador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;

import ec.gerontologia.modelo.Encargado;
import ec.gerontologia.modelo.EncargadoDAO;
import ec.gerontologia.modelo.Menu;
import ec.gerontologia.modelo.MenuDAO;
import ec.gerontologia.modelo.Permiso;
import ec.gerontologia.modelo.PermisoDAO;
import ec.gerontologia.modelo.Usuario;
import ec.gerontologia.modelo.UsuarioDAO;
import ec.gerontologia.util.Constantes;
import ec.gerontologia.util.Context;
import ec.gerontologia.util.SecurityUtil;

public class MenuControlador {

	@Wire Tree menu;
	@Wire Include areaContenido;
	
	Menu opcionSeleccionado;
	UsuarioDAO usuarioDAO = new UsuarioDAO();
	PermisoDAO permisoDAO = new PermisoDAO();
	MenuDAO menuDAO = new MenuDAO();
	List<Menu> listaPermisosPadre = new ArrayList<Menu>();
	List<Permiso> listaPermisosTodos = new ArrayList<Permiso>();
	
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException, MessagingException{
		Selectors.wireComponents(view, this, false);
		loadTree();
		//startLongOperation();
	}
	
	public void loadTree() throws IOException{		
		Usuario usuario = usuarioDAO.getUsuario(SecurityUtil.getUser().getUsername().trim()); 
		if (usuario != null){
			//listaPermisosPadre = permisoDAO.getListaPermisosPadre(usuario.getPerfil().getIdPerfil());
			Context.getInstance().setUsuarioLogeado(usuario);
			listaPermisosTodos = permisoDAO.getListaPermisosTodos(usuario.getPerfil().getIdPerfil());//solo busca los permisos hijos
			//traer los padres de los hijos
			for(Permiso per : listaPermisosTodos) {
				List<Menu> menu = menuDAO.getMenuById(per.getMenu().getIdMenuPadre());
				if(menu.size() > 0) {
					boolean bandera = false;
					for(Menu padre : listaPermisosPadre) {
						if(padre.getIdMenu() == menu.get(0).getIdMenu())
							bandera = true;
					}
					if(bandera == false)
						listaPermisosPadre.add(menu.get(0));
				}
			}
			
			if (listaPermisosPadre.size() > 0) { //si tiene permisos el usuario
				//capturar solo los menus
				List<Menu> listaMenu = new ArrayList<Menu>();
				for(Menu permiso : listaPermisosPadre) listaMenu.add(permiso);
				menu.appendChild(getTreechildren(listaMenu));   
			}			
		}
		listaPermisosPadre = null;
	}
	private Treechildren getTreechildren(List<Menu> listaMenu) {
		Treechildren retorno = new Treechildren();
		for(Menu opcion : listaMenu) {
			Treeitem ti = getTreeitem(opcion);
			retorno.appendChild(ti);
			List<Menu> listaPadreHijo = new ArrayList<Menu>();
			for(Permiso permiso : listaPermisosTodos) {
				if(permiso.getMenu().getIdMenuPadre() == opcion.getIdMenu())
					listaPadreHijo.add(permiso.getMenu());
			}
			if (!listaPadreHijo.isEmpty()) {
				ti.appendChild(getTreechildren(listaPadreHijo));
			}
		}
		return retorno;
	}
	@SuppressWarnings({ "rawtypes", "deprecation", "unchecked" })
	private Treeitem getTreeitem(Menu opcion) {
		Treeitem retorno = new Treeitem();
		Treerow tr = new Treerow();
		Treecell tc = new Treecell(opcion.getDescripcion());
		//System.out.println("titulomenu: " + tc);
		if (opcion.getIcono() != null) {
			//tc.setIconSclass(opcion.getImagen());
			tc.setSrc(opcion.getIcono());
		}
		tr.addEventListener(Events.ON_CLICK, new EventListener() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				// TODO Auto-generated method stub
				opcionSeleccionado = opcion; 
				if(opcionSeleccionado.getUrl() != null){
					loadContenido(opcionSeleccionado);
				}
			}
		});
		
		tr.appendChild(tc);
		retorno.appendChild(tr);
		retorno.setOpen(false);
		return retorno;
	}
	@NotifyChange({"areaContenido"})
	public void loadContenido(Menu opcion) {
		
		if (opcion.getUrl().toLowerCase().substring(0, 2).toLowerCase().equals("http")) {
			Sessions.getCurrent().setAttribute("FormularioActual", null);
			Executions.getCurrent().sendRedirect(opcion.getUrl(), "_blank");			
		} else {
			Sessions.getCurrent().setAttribute("FormularioActual", opcion);	
			areaContenido.setSrc(opcion.getUrl());
		}	
		
	}
	public String getNombreUsuario() {
		return SecurityUtil.getUser().getUsername();
	}
	EncargadoDAO encargadoDAO = new EncargadoDAO();
	public String getNombreCentro() {
		String valor = "";
		if(Context.getInstance().getUsuarioLogeado().getPerfil().getIdPerfil() == Constantes.ID_USUARIO_ADMIN) {
			valor = "SISTEMA DE CONTROL GERONTOLÓGICO - ADMINISTRADOR";
		}else {
			List<Encargado> encargado = encargadoDAO.getEncargadosUsuario();
			if(encargado.size() > 0)
				valor = valor + "SISTEMA DE CONTROL GERONTOLÓGICO - CENTRO " + encargado.get(0).getCentro().getNombreCentro();
			else
				valor = valor + "SISTEMA DE CONTROL GERONTOLÓGICO - NO TIENE ASIGNADO CENTRO";
		}
		return valor;
	}
}
