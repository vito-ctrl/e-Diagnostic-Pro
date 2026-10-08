<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="title" value="Mon Profil - Médecin Spécialiste" />
</jsp:include>

<div class="max-w-2xl mx-auto space-y-6">
    <div class="bg-white p-6 rounded-2xl border border-slate-200 shadow-sm">
        <h1 class="text-2xl font-bold text-slate-900">US5 : Configuration de mon Profil</h1>
        <p class="mt-1 text-sm text-slate-500">
            Personnalisez votre spécialité médicale et votre tarif de télé-expertise
        </p>
    </div>

    <c:if test="${param.success == '1'}">
        <div class="p-4 rounded-xl bg-emerald-50 border border-emerald-200 text-emerald-800 text-sm flex items-center space-x-2">
            <i class="fa-solid fa-circle-check text-emerald-600 text-lg"></i>
            <span>Votre profil a été mis à jour avec succès !</span>
        </div>
    </c:if>

    <div class="bg-white p-6 rounded-2xl border border-slate-200 shadow-sm">
        <form action="${pageContext.request.contextPath}/specialiste/profil" method="POST" class="space-y-5">
            <input type="hidden" name="csrfToken" value="${csrfToken}" />

            <div>
                <label class="block text-xs font-semibold text-slate-700 uppercase mb-1">Médecin Spécialiste</label>
                <input type="text" disabled value="${specialiste.nomComplet}"
                       class="w-full px-3 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-sm font-semibold text-slate-600" />
            </div>

            <div>
                <label class="block text-xs font-semibold text-slate-700 uppercase mb-1">Spécialité Médicale *</label>
                <select name="specialite" required
                        class="w-full px-3 py-2.5 border border-slate-300 rounded-xl text-sm focus:ring-2 focus:ring-purple-500 font-medium">
                    <c:forEach var="sp" items="${specialites}">
                        <option value="${sp.name()}" ${specialiste.specialite == sp ? 'selected' : ''}>
                            ${sp.libelle}
                        </option>
                    </c:forEach>
                </select>
            </div>

            <div>
                <label class="block text-xs font-semibold text-slate-700 uppercase mb-1">Tarif de consultation / télé-expertise (en DH) *</label>
                <div class="relative">
                    <input type="number" step="10" name="tarif" required min="50" max="2000"
                           value="${specialiste.tarifConsultation}"
                           class="w-full px-3 py-2.5 border border-slate-300 rounded-xl text-sm focus:ring-2 focus:ring-purple-500 font-mono font-bold" />
                    <span class="absolute inset-y-0 right-0 pr-3 flex items-center pointer-events-none text-slate-400 font-semibold text-xs">
                        DH
                    </span>
                </div>
                <span class="text-xs text-slate-400 mt-1 block">Tarif appliqué par défaut pour toute demande d'expertise</span>
            </div>

            <div>
                <label class="block text-xs font-semibold text-slate-700 uppercase mb-1">Durée moyenne de consultation</label>
                <input type="text" disabled value="30 minutes (Fixe)"
                       class="w-full px-3 py-2 bg-slate-50 border border-slate-200 rounded-xl text-sm text-slate-500" />
            </div>

            <div class="pt-2 flex justify-end">
                <button type="submit"
                        class="px-6 py-2.5 bg-purple-600 hover:bg-purple-700 text-white rounded-xl text-sm font-bold shadow-md transition flex items-center space-x-2">
                    <i class="fa-solid fa-floppy-disk"></i>
                    <span>Enregistrer les modifications</span>
                </button>
            </div>
        </form>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
