<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="title" value="Connexion - Télé-Expertise Médicale" />
</jsp:include>

<div class="min-h-[75vh] flex items-center justify-center py-12 px-4 sm:px-6 lg:px-8">
    <div class="max-w-md w-full space-y-8 bg-white p-8 rounded-2xl shadow-lg border border-slate-100">
        <div class="text-center">
            <div class="mx-auto h-16 w-16 bg-sky-600 rounded-2xl flex items-center justify-center text-white shadow-lg shadow-sky-200">
                <i class="fa-solid fa-hospital text-3xl"></i>
            </div>
            <h2 class="mt-4 text-2xl font-bold tracking-tight text-slate-900">
                Télé-Expertise Médicale
            </h2>
            <p class="mt-1 text-sm text-slate-500">
                Portail d'authentification professionnel de santé
            </p>
        </div>

        <c:if test="${not empty error}">
            <div class="p-4 rounded-xl bg-red-50 border border-red-200 text-red-700 text-sm flex items-center space-x-2">
                <i class="fa-solid fa-circle-exclamation text-red-500"></i>
                <span>${error}</span>
            </div>
        </c:if>

        <c:if test="${param.logout == '1'}">
            <div class="p-4 rounded-xl bg-emerald-50 border border-emerald-200 text-emerald-700 text-sm flex items-center space-x-2">
                <i class="fa-solid fa-check-circle text-emerald-500"></i>
                <span>Vous avez été déconnecté avec succès.</span>
            </div>
        </c:if>

        <form class="mt-8 space-y-5" action="${pageContext.request.contextPath}/login" method="POST">
            <input type="hidden" name="csrfToken" value="${csrfToken}" />

            <div>
                <label for="username" class="block text-sm font-medium text-slate-700 mb-1">Nom d'utilisateur</label>
                <div class="relative">
                    <span class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none text-slate-400">
                        <i class="fa-solid fa-user"></i>
                    </span>
                    <input id="username" name="username" type="text" required value="${username}"
                           class="w-full pl-10 pr-3 py-2.5 border border-slate-300 rounded-xl text-slate-900 placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-sky-500 focus:border-sky-500 text-sm"
                           placeholder="ex: infirmier, generaliste, cardio..." />
                </div>
            </div>

            <div>
                <label for="password" class="block text-sm font-medium text-slate-700 mb-1">Mot de passe</label>
                <div class="relative">
                    <span class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none text-slate-400">
                        <i class="fa-solid fa-lock"></i>
                    </span>
                    <input id="password" name="password" type="password" required
                           class="w-full pl-10 pr-3 py-2.5 border border-slate-300 rounded-xl text-slate-900 placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-sky-500 focus:border-sky-500 text-sm"
                           placeholder="••••••••" />
                </div>
            </div>

            <div>
                <button type="submit"
                        class="w-full flex justify-center py-2.5 px-4 border border-transparent rounded-xl shadow-sm text-sm font-semibold text-white bg-sky-600 hover:bg-sky-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-sky-500 transition">
                    Se connecter
                </button>
            </div>
        </form>

        <!-- Quick Credentials Helper -->
        <div class="mt-6 pt-6 border-t border-slate-100">
            <p class="text-xs font-semibold text-slate-500 uppercase tracking-wider mb-2 text-center">Comptes de démonstration (cliquer pour remplir) :</p>
            <div class="grid grid-cols-2 gap-2 text-xs">
                <button type="button" onclick="fillLogin('infirmier', 'password123')"
                        class="p-2 bg-emerald-50 hover:bg-emerald-100 text-emerald-800 rounded-lg text-left border border-emerald-200 transition">
                    <strong>Infirmier</strong><br><span class="text-slate-500">infirmier</span>
                </button>
                <button type="button" onclick="fillLogin('generaliste', 'password123')"
                        class="p-2 bg-sky-50 hover:bg-sky-100 text-sky-800 rounded-lg text-left border border-sky-200 transition">
                    <strong>Généraliste</strong><br><span class="text-slate-500">generaliste</span>
                </button>
                <button type="button" onclick="fillLogin('cardio', 'password123')"
                        class="p-2 bg-purple-50 hover:bg-purple-100 text-purple-800 rounded-lg text-left border border-purple-200 transition">
                    <strong>Spécialiste Cardio</strong><br><span class="text-slate-500">cardio</span>
                </button>
                <button type="button" onclick="fillLogin('admin', 'admin123')"
                        class="p-2 bg-amber-50 hover:bg-amber-100 text-amber-800 rounded-lg text-left border border-amber-200 transition">
                    <strong>Admin</strong><br><span class="text-slate-500">admin</span>
                </button>
            </div>
        </div>
    </div>
</div>

<script>
    function fillLogin(u, p) {
        document.getElementById('username').value = u;
        document.getElementById('password').value = p;
    }
</script>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
