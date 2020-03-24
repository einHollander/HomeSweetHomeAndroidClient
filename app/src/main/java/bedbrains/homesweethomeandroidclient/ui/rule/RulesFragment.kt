package bedbrains.homesweethomeandroidclient.ui.rule

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import bedbrains.homesweethomeandroidclient.DataRepository
import bedbrains.homesweethomeandroidclient.R
import bedbrains.homesweethomeandroidclient.databinding.FragmentRulesBinding
import bedbrains.homesweethomeandroidclient.rest.Resp

class RulesFragment : Fragment() {

    private val rulesViewModel: RulesViewModel by viewModels()
    private lateinit var binding: FragmentRulesBinding
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)

        binding = FragmentRulesBinding.inflate(inflater)
        swipeRefreshLayout = binding.swipeRefreshLayout

        val rules = binding.rules
        val addButton = binding.addButton
        val linearLayoutManager = LinearLayoutManager(context)
        val ruleListAdapter = RuleListAdapter(rulesViewModel.rules.value!!)

        rules.layoutManager = linearLayoutManager
        rules.adapter = ruleListAdapter

        rulesViewModel.rules.observe(viewLifecycleOwner, Observer {
            ruleListAdapter.updateRules(it)
        })

        swipeRefreshLayout.setOnRefreshListener {
            DataRepository.updateRules().observe(viewLifecycleOwner, Observer {
                if (it == Resp.FAILURE) {
                    Toast.makeText(context, R.string.resp_update_error, Toast.LENGTH_SHORT).show()
                }
                swipeRefreshLayout.isRefreshing = false
            })
        }

        addButton.setOnClickListener {
            binding.root.findNavController().navigate(R.id.action_nav_rules_to_nav_weekly_rule)
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.rules, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_refresh -> {
                swipeRefreshLayout.isRefreshing = true
                DataRepository.updateRules().observe(viewLifecycleOwner, Observer {
                    if (it == Resp.FAILURE) {
                        Toast.makeText(context, R.string.resp_update_error, Toast.LENGTH_SHORT).show()
                    }
                    swipeRefreshLayout.isRefreshing = false
                })
            }
            R.id.action_edit -> {
                //TODO
            }
            R.id.action_sort_by -> {
                //TODO
            }
            R.id.action_group_by -> {
                //TODO
            }
            else -> return super.onOptionsItemSelected(item)
        }

        return false
    }
}