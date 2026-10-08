<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="title" value="Gestion du Staff - Administrateur" />
</jsp:include>

<div class="space-y-6">
    <div class="sm:flex sm:items-center sm:justify-between bg-white p-6 rounded-2xl border border-slate-200 shadow-sm">
        <div>
            <h1 class="text-2xl font-bold text-slate-900">Administration &bull; Gestion du Staff Médical</h1>
            <p class="mt-1 text-sm text-slate-500">
                Création et gestion des comptes utilisateurs (Infirmiers, Généralistes, Spécialistes)
            </p>
        </div>
        <button type="button" onclick="document.getElementById('modalAddStaff').classList.remove('hidden')"
                class="mt-4 sm:mt-0 px-4 py-2 bg-amber-600 hover:bg-amber-700 text-white rounded-xl text-sm font-semibold shadow-sm transition flex items-center space-x-1.5">
            <i class="fa-solid fa-user-plus"></i>
            <span>Ajouter un membre du staff</span>
        </button>
    </div>

    <c:if test="${param.success == '1'}">
        <div class="p-4 rounded-xl bg-emerald-50 border border-emerald-200 text-emerald-800 text-sm flex items-center space-x-2">
            <i class="fa-solid fa-circle-check text-emerald-600 text-lg"></i>
            <span>Nouveau membre du personnel ajouté avec succès (mot de passe haché en BCrypt).</span>
        </div>
    </c:if>

    <!-- Staff List Table -->
    <div class="bg-white rounded-2xl border border-slate-200 shadow-sm overflow-hidden">
        <div class="overflow-x-auto">
            <table class="min-w-full divide-y divide-slate-200 text-left text-sm">
                <thead class="bg-slate-50 text-slate-500 uppercase font-semibold text-xs tracking-wider">
                    <tr>
                        <th class="px-6 py-4">Nom Complet</th>
                        <th class="px-6 py-4">Identifiant</th>
                        <th class="px-6 py-4">Email</th>
                        <th class="px-6 py-4">Rôle</th>
                        <th class="px-6 py-4">Spécialité & Tarif</th>
                        <th class="px-6 py-4">Statut</th>
                    </tr>
                </thead>
                <tbody class="divide-y divide-slate-100 bg-white">
                    <c:forEach var="u" items="${staffList}">
                        <tr class="hover:bg-slate-50/80 transition">
                            <td class="px-6 py-4 whitespace-nowrap font-bold text-slate-900">
                                ${u.nomComplet}
                            </td>
                            <td class="px-6 py-4 whitespace-nowrap font-mono text-xs text-slate-600">
                                ${u.username}
                            </td>
                            <td class="px-6 py-4 whitespace-nowrap text-xs text-slate-600">
                                ${u.email}
                            </td>
                            <td class="px-6 py-4 whitespace-nowrap">
                                <span class="px-2.5 py-1 text-xs rounded-full font-semibold
                                    <c:choose>
                                        <c:when test="${u.role == 'INFIRMIER'}">bg-emerald-100 text-emerald-800</c:when>
                                        <c:when test="${u.role == 'GENERALISTE'}">bg-sky-100 text-sky-800</c:when>
                                        <c:when test="${u.role == 'SPECIALISTE'}">bg-purple-100 text-purple-800</c:when>
                                        <c:otherwise>bg-amber-100 text-amber-800</c:otherwise>
                                    </c:choose>">
                                    ${u.role.libelle}
                                </span>
                            </td>
                            <td class="px-6 py-4 whitespace-nowrap text-xs">
                                <c:choose>
                                    <c:when test="${u.role == 'SPECIALISTE'}">
                                        <span class="font-medium text-slate-800">${u.specialite != null ? u.specialite.libelle : '-'}</span>
                                        <span class="text-slate-400">&bull;</span>
                                        <span class="font-bold text-purple-700 font-mono">${u.tarifConsultation} DH</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="text-slate-400 italic">-</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="px-6 py-4 whitespace-nowrap">
                                <span class="inline-flex items-center text-xs font-semibold ${u.actif ? 'text-emerald-700' : 'text-slate-400'}">
                                    <span class="h-2 w-2 rounded-full ${u.actif ? 'bg-emerald-500' : 'bg-slate-300'} mr-1.5"></span>
                                    ${u.actif ? 'Actif' : 'Désactivé'}
                                </span>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<!-- Modal Add Staff -->
<div id="modalAddStaff" class="fixed inset-0 bg-slate-900/50 backdrop-blur-sm z-50 flex items-center justify-center p-4 hidden">
    <div class="bg-white rounded-2xl max-w-lg w-full p-6 shadow-2xl border border-slate-100 space-y-4">
        <div class="flex items-center justify-between border-b border-slate-100 pb-3">
            <h3 class="text-lg font-bold text-slate-900">Ajouter un nouveau membre du staff</h3>
            <button type="button" onclick="document.getElementById('modalAddStaff').classList.add('hidden')"
                    class="text-slate-400 hover:text-slate-600">
                <i class="fa-solid fa-xmark text-lg"></i>
            </button>
        </div>

        <form action="${pageContext.request.contextPath}/admin/staff/creer" method="POST" class="space-y-4">
            <input type="hidden" name="csrfToken" value="${csrfToken}" />

            <div class="grid grid-cols-2 gap-3">
                <div>
                    <label class="block text-xs font-semibold text-slate-700 uppercase mb-1">Nom *</label>
                    <input type="text" name="nom" required class="w-full px-3 py-2 border border-slate-300 rounded-xl text-sm" />
                </div>
                <div>
                    <label class="block text-xs font-semibold text-slate-700 uppercase mb-1">Prénom *</label>
                    <input type="text" name="prenom" required class="w-full px-3 py-2 border border-slate-300 rounded-xl text-sm" />
                </div>
            </div>

            <div class="grid grid-cols-2 gap-3">
                <div>
                    <label class="block text-xs font-semibold text-slate-700 uppercase mb-1">Identifiant (login) *</label>
                    <input type="text" name="username" required class="w-full px-3 py-2 border border-slate-300 rounded-xl text-sm" />
                </div>
                <div>
                    <label class="block text-xs font-semibold text-slate-700 uppercase mb-1">Mot de passe *</label>
                    <input type="password" name="password" required class="w-full px-3 py-2 border border-slate-300 rounded-xl text-sm" />
                </div>
            </div>

            <div>
                <label class="block text-xs font-semibold text-slate-700 uppercase mb-1">Email *</label>
                <input type="email" name="email" required class="w-full px-3 py-2 border border-slate-300 rounded-xl text-sm" />
            </div>

            <div>
                <label class="block text-xs font-semibold text-slate-700 uppercase mb-1">Rôle *</label>
                <select name="role" id="modalRoleSelect" required onchange="toggleSpecialistFields(this.value)"
                        class="w-full px-3 py-2 border border-slate-300 rounded-xl text-sm font-medium">
                    <c:forEach var="r" items="${roles}">
                        <option value="${r.name()}">${r.libelle}</option>
                    </c:forEach>
                </select>
            </div>

            <div id="modalSpecialistDiv" class="grid grid-cols-2 gap-3 hidden pt-2 border-t border-slate-100">
                <div>
                    <label class="block text-xs font-semibold text-slate-700 uppercase mb-1">Spécialité</label>
                    <select name="specialite" class="w-full px-3 py-2 border border-slate-300 rounded-xl text-sm">
                        <c:forEach var="sp" items="${specialites}">
                            <option value="${sp.name()}">${sp.libelle}</option>
                        </c:forEach>
                    </select>
                </div>
                <div>
                    <label class="block text-xs font-semibold text-slate-700 uppercase mb-1">Tarif (DH)</label>
                    <input type="number" name="tarif" value="250" class="w-full px-3 py-2 border border-slate-300 rounded-xl text-sm" />
                </div>
            </div>

            <div class="flex justify-end space-x-2 pt-3 border-t border-slate-100">
                <button type="button" onclick="document.getElementById('modalAddStaff').classList.add('hidden')"
                        class="px-4 py-2 bg-slate-100 hover:bg-slate-200 text-slate-700 rounded-xl text-sm font-semibold transition">
                    Annuler
                </button>
                <button type="submit"
                        class="px-5 py-2 bg-amber-600 hover:bg-amber-700 text-white rounded-xl text-sm font-semibold shadow-sm transition">
                    Créer le compte
                </button>
            </div>
        </form>
    </div>
</div>

<script>
    function toggleSpecialistFields(role) {
        const div = document.getElementById('modalSpecialistDiv');
        if (role === 'SPECIALISTE') {
            div.classList.remove('hidden');
        } else {
            div.classList.add('hidden');
        }
    }
</script>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
