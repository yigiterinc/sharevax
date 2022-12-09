import {useEffect, useState} from 'react';
import Paper from '@mui/material/Paper';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TablePagination from '@mui/material/TablePagination';
import TableRow from '@mui/material/TableRow';
import {fetchActiveDeliveries} from '../services/services';

const tableHeader = [
	{id: 'destinationHarbor1', label: 'Destination', minWidth: 10},
	{id: 'startHarbor1', label: 'From', minWidth: 21},
	{id: 'vaccineType', label: 'Vaccine', minWidth: 10},
	{id: 'quantity', label: 'Dose', minWidth: 20},
	{id: 'createdAt', label: 'Order Date', minWidth: 21},
	{id: 'estimatedArrivalDate', label: 'Estimated Arrival Date', minWidth: 21},
	{id: 'remainingDaysToNextHarbor', label: 'Current Arrival Date', minWidth: 21},
	{id: 'deliveryStatus', label: 'Status', minWidth: 21},
	{id: 'urgency', label: 'Urgency', minWidth: 21},
];

export default function OverviewTable() {
	const [page, setPage] = useState(0);
	const [rowsPerPage, setRowsPerPage] = useState(10);
	const [activeDeliveriesData, setActiveDeliveriesData] = useState([]);
	const [loading, setLoading] = useState(true);

	useEffect(() => {
		fetchActiveDeliveriesData();
	}, []);

	const fetchActiveDeliveriesData = async () => {
		const result = await fetchActiveDeliveries();
		setActiveDeliveriesData(result.data);
		setLoading(false);
	};

	const handleChangePage = (event, newPage) => {
		setPage(newPage);
	};

	const handleChangeRowsPerPage = (event) => {
		setRowsPerPage(+event.target.value);
		setPage(0);
	};

	console.log('New json\n', activeDeliveriesData);

	// for (let i = 1; i < activeDeliveriesData.length(); i++) {
	// 	rowData.push(activeDeliveriesData[i]);
	// }

	return (
		<Paper sx={{width: '100%', overflow: 'hidden'}}>
			<div className='text-bg text-white p-2 bg-black font-mono font-bold'>Vaccines Currently Shipping</div>
			<TableContainer sx={{maxHeight: 260}}>
				<Table stickyHeader aria-label='sticky table'>
					<TableHead>
						{!loading && (
							<TableRow>
								{tableHeader.map((column) => (
									<TableCell
										key={column.id}
										align={column.align}
										style={{minWidth: column.minWidth}}
										className='font-bold text-white bg-black border-t border-gray-500'
									>
										{column.label}
									</TableCell>
								))}
							</TableRow>
						)}
					</TableHead>

					<TableBody>
						{activeDeliveriesData.slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage).map((row) => {
							return (
								<TableRow hover role='checkbox' tabIndex={-1} key={row.code}>
									{tableHeader.map((column) => {
										const value = row[column.id];
										return (
											<TableCell key={column.id} align={column.align} className='font-mono'>
												{column.format && typeof value === 'number' ? column.format(value) : value}
											</TableCell>
										);
									})}
								</TableRow>
							);
						})}
					</TableBody>
				</Table>
			</TableContainer>
			<TablePagination
				rowsPerPageOptions={[5, 10, 20]}
				component='div'
				count={activeDeliveriesData.length}
				rowsPerPage={rowsPerPage}
				page={page}
				onPageChange={handleChangePage}
				onRowsPerPageChange={handleChangeRowsPerPage}
			/>
		</Paper>
	);
}
