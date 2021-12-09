package org.inc.tsmgr.account

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/account")
class AccountController(
    private val service: AccountService
) {

    @GetMapping("/")
    fun accounts(): Iterable<Account> = service.getAccounts()

    @GetMapping("/{id}")
    fun account(@RequestParam id: String): Account? = service.getAccount(id)

    @PostMapping("/import")
    fun import(@RequestBody accounts: Accounts): String = service.import(accounts)

    @PostMapping("/export/json")
    fun exportJson(): String = service.exportJson()


}