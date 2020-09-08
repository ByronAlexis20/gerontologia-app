package ec.gerontologia.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class PerfilDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<Perfil> getPerfiles() {
		List<Perfil> resultado = new ArrayList<Perfil>(); 
		Query query = getEntityManager().createNamedQuery("Perfil.perfilActivo");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<Perfil>) query.getResultList();
		return resultado;
	}
}
